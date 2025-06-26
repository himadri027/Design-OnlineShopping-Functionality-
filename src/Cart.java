import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cart {


    private final Map< Product , Integer > items ;

    public Cart() {
        items = new ConcurrentHashMap<>();
    }
    public Map<Product, Integer> getItems() {
        return items;
    }

    // add item , remove , update ;
    public void addItem (Product product , int quantity ) {
        //merge execute withoout interrupptiom
        items.merge(product , quantity, Integer::sum ) ;
    }

    public void removeItem (Product product , int quantity ) {
        items.computeIfPresent(product, (key, oldQty) -> {
            int updatedQty = oldQty - quantity;
            return updatedQty > 0 ? updatedQty : null; // null removes the key
        });
    }

    public void clear() {
        items.clear();
    }


}
