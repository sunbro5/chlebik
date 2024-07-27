package cz.jan.order.model;

import java.util.List;

public record CreateOrderRequest(
        List<CreateOrderItemRequest> items
) {
}
