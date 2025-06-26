import java.util.UUID;

public class Product {

    private final String id;
    private final String name;
    private final String description;
    private final double price;
    private int stock;


    public Product( String name, String description, double price ,int stock) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock ;
    }



    public synchronized  void increaseStock(int qunatity) {
        stock += qunatity ;
    }
    public synchronized  void decreaseStock(int qunatity) {
        if ( stock < qunatity ) new IllegalStateException("not having enough stock ") ;
        stock -= qunatity ;
    }
    public synchronized void updateQuantity(int quantity) {
        this.stock += quantity;
    }
    public synchronized  boolean isAvailable(int qualitty) {
        return this.stock >= qualitty ;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }



}
