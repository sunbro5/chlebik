package cz.jan.order.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderProductQuantityException extends RuntimeException {

    private final List<String> productErrors;

    public OrderProductQuantityException(List<String> productErrors) {
        this.productErrors = productErrors;
    }
}
