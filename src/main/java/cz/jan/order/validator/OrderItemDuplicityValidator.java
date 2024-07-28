package cz.jan.order.validator;

import cz.jan.order.model.CreateOrderItemRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OrderItemDuplicityValidator implements ConstraintValidator<OrderItemDuplicityValid,
        List<CreateOrderItemRequest>> {

    @Override
    public boolean isValid(List<CreateOrderItemRequest> value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        Set<Long> productUniqueIds = new HashSet<>();
        return value.stream()
                .map(CreateOrderItemRequest::productId)
                .allMatch(productUniqueIds::add);
    }
}
