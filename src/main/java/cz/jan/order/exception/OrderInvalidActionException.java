package cz.jan.order.exception;


public class OrderInvalidActionException extends RuntimeException {

    public OrderInvalidActionException(String message) {
        super(message);
    }
}
