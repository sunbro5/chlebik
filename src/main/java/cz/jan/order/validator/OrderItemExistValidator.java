package cz.jan.order.validator;

import cz.jan.order.model.CreateOrderItemRequest;
import cz.jan.product.ProductService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderItemExistValidator implements ConstraintValidator<OrderItemExistValid, CreateOrderItemRequest> {

    @Autowired
    private ProductService productService;

    @Override
    public boolean isValid(CreateOrderItemRequest value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return productService.getActiveProduct(value.productId()).isPresent(); //TODO use cache to get active products
    }
}
