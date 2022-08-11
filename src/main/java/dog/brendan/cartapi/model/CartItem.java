package dog.brendan.cartapi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("cartItems")
public class CartItem {

    @Id
    public String id;

    public String product;

    public double price;

    public int quantity;

    public CartItem(String product, double price, int quantity) {
        this.product = product;
        this.price = price;
        this.quantity = quantity;
    }

}
