package dog.brendan.cartapi;

import dog.brendan.cartapi.model.CartItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CartItemRepository extends MongoRepository<CartItem, String> {

    List<CartItem> findAll();

    List<CartItem> findByProduct(String product);

    List<CartItem> findByProductStartingWith(String prefix);

    List<CartItem> findByPriceLessThan(Double price);
}
