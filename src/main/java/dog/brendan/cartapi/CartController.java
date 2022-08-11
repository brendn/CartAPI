package dog.brendan.cartapi;

import dog.brendan.cartapi.exception.ItemNotFoundException;
import dog.brendan.cartapi.model.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {

    @Autowired
    private CartItemRepository repo;

    @GetMapping("/cart-items")
    public List<CartItem> readAll(@RequestParam(required = false) String product,
                                  @RequestParam(required = false) Double maxPrice,
                                  @RequestParam(required = false) String prefix,
                                  @RequestParam(required = false) Integer pageSize) {
        if (product != null) {
            return repo.findByProduct(product);
        }
        if (maxPrice != null) {
            return repo.findByPriceLessThan(maxPrice);
        }
        if (prefix != null) {
            return repo.findByProductStartingWith(prefix);
        }
        if (pageSize != null) {
            List<CartItem> items = repo.findAll();
            return items.subList(0, pageSize);
        }
        return repo.findAll();
    }

    @GetMapping("/reset")
    public String reset() {
        repo.deleteAll();
        repo.insert(new CartItem("Pizza", 16.99, 1));
        repo.insert(new CartItem("Smoothie", 6.99, 1));
        repo.insert(new CartItem("BBQ Pork Rinds", 2.99, 5));
        repo.insert(new CartItem("Trident", 1.99, 2));
        return "Reset successfully!";
    }

    @GetMapping("/cart-items/{id}")
    public CartItem readOne(@PathVariable("id") String id) {
        return repo.findById(id).orElseThrow(() -> new ItemNotFoundException(String.format("Item ID %s not found", id)));
    }

    @PostMapping("/cart-items")
    @ResponseStatus(HttpStatus.CREATED)
    public CartItem insertOne(@RequestBody CartItem cartItem) {
        repo.insert(cartItem);
        return cartItem;
    }

    @DeleteMapping("/cart-items/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        repo.deleteById(id);
    }

    @ResponseBody
    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFound(ItemNotFoundException ex) {
        return ex.getMessage();
    }

    @GetMapping("/cart-items/total-cost")
    public double getTotalCost() {
        List<CartItem> items = repo.findAll();
        return items.stream().mapToDouble(i -> (i.price * i.quantity)).sum() * 1.06;
    }

    @PutMapping("/cart-items/{id}")
    public CartItem updateItem(@PathVariable("id") String id, @RequestBody CartItem item) {
        item.id = id;
        repo.save(item);
        return item;
    }

    @PatchMapping("/cart-items/{id}/add")
    public CartItem updateAmount(@PathVariable("id") String id, @RequestParam int count) {
        CartItem item = repo.findById(id).orElseThrow(() -> new ItemNotFoundException(String.format("Item ID %s not found", id)));
        item.quantity += count;
        repo.save(item);
        return item;
    }

}
