package cz.jan.order.model;

import java.util.List;

public record Order(
        Long orderId,
        OrderStateType state,
        List<OrderItem> items
) {
}
