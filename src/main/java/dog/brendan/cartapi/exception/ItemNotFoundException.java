package dog.brendan.cartapi.exception;

public class ItemNotFoundException extends RuntimeException {

    static final long serialVersionUID = 1L;

    public ItemNotFoundException(String s) {
        super(s);
    }
}
