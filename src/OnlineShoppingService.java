import Payment.PaymentContext;
import Payment.PaymentStratergy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class OnlineShoppingService {

    private static OnlineShoppingService instance ;
    private final Map<String, User> users;
    private final Map<String, Product> products;
    private final Map<String, Order> orders;


    private OnlineShoppingService() {
        users = new ConcurrentHashMap<>();
        products = new ConcurrentHashMap<>();
        orders = new ConcurrentHashMap<>();
    }


    public static synchronized OnlineShoppingService getInstance() {
        if (instance == null) {
            instance = new OnlineShoppingService();
        }
        return instance;
    }

    public User registerUser (  String name, String email,  String password){
        User user = new User(name, email, password);
        users.put(user.getId(), user);
        return user;
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public Product addProduct(String name, String description, double price, int stock) {
        Product product = new Product(name, description, price, stock);
        products.put(product.getId(), product);
        return product;
    }

    public void  addtoCart (String userId , String  productId  , int quantity){
        User user = users.get(userId) ;
        Product product = products.get(productId)  ;
        if ( user == null || product == null ) {
            throw new IllegalArgumentException("User or Product not found  ");
        }
        user.getCart().addItem( product , quantity);
    }
    public void  removeFromCart (String userId , String  productId  , int quantity){
        User user = users.get(userId) ;
        Product product = products.get(productId)  ;
        if ( user == null || product == null ) {
            throw new IllegalArgumentException("User or Product not found  ");
        }
        user.getCart().removeItem( product , quantity);
    }
    public  Cart getUserCart ( String userId ){
        User user = users.get(userId);
        return  user.getCart() ;
    }
    public Product getProduct(String productId) {
        return products.get(productId);
    }
    public List< Product > searchProduct (String keyword ){
        return products.values().stream()
                .filter(product -> product.getName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }





    public synchronized Order placeOrder ( String userId ,PaymentStratergy paymentStrategy ) {
        User user = users.get(userId);
        if (user == null) throw new IllegalArgumentException("User not found");

        List < OrderItem > orderItems = new ArrayList<>();
        Map<Product, Integer> items = user.getCart().getItems();

        for ( Map.Entry< Product , Integer >  entry : items.entrySet()) {
            Product product = entry.getKey() ;
            Integer quantity  = entry.getValue() ;

            if( product.isAvailable(quantity)){
                product.decreaseStock(quantity);
                orderItems.add(new OrderItem(product , quantity)) ;
            }
        }
        Order order = new Order(user  , orderItems) ;
        orders.put(order.getId() , order) ;
        user.getCart().clear();
        user.addOrder(order);

        double amountToPay = order.getTotalAmount();

        PaymentContext paymentContext = new PaymentContext();
        paymentContext.setStrategy(paymentStrategy);

        System.out.println(" Initiating payment of ₹" +amountToPay );
        if (paymentContext.executePayment(amountToPay)){
            System.out.println("Payment Successfull");
            order.setStatus(OrderStatus.PLACED);
        }
        else {
            order.setStatus(OrderStatus.CANCELLED);
            for (OrderItem item : orderItems) {
                Product product = item.getProduct();
                int quantity = item.getQuantity();
                product.increaseStock(quantity);
            }

        }
        return  order;

    }
    public synchronized void cancelOrder(String orderId) {
        Order order = orders.get(orderId);
        if (order == null) throw new IllegalArgumentException("Order not found");

        order.cancel();
        for (OrderItem orderItem : order.getItems()) {
            orderItem.getProduct().increaseStock(orderItem.getQuantity());
        }
    }

    public Order getOrder(String orderId) {
        return orders.get(orderId);
    }

    private String generateOrderId() {
        return "ORDER" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }




















}
