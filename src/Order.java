import java.util.List;
import java.util.UUID;

public class Order {

    private  final  String id ;
    private final User user;
    private  final  double totalAmount ;
    private final List < OrderItem > items ;
    private OrderStatus status ;


    public Order(User user,  List<OrderItem> items) {
        this.id = UUID.randomUUID().toString();
        this.user = user;
        this.items = items;
        this.totalAmount = calculateTotalAmount();
        this.status = OrderStatus.PENDING;
    }

    private double calculateTotalAmount() {
        return items.stream().mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity()).sum() ;
    }
    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
    public void cancel() {
        if (status == OrderStatus.SHIPPED)
            throw new IllegalStateException("Cannot cancel shipped order");
        status = OrderStatus.CANCELLED;
    }



}
