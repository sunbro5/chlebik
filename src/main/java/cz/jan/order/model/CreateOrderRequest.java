package cz.jan.order.model;

import cz.jan.order.validator.OrderItemDuplicityValid;
import cz.jan.order.validator.OrderItemExistValid;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateOrderRequest(
        @Size(min = 1)
        @OrderItemDuplicityValid
        List<@OrderItemExistValid CreateOrderItemRequest> items
) {
}
