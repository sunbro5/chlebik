package cz.jan.order.model;

import java.util.List;

public record Order(
        long orderId,
        List<OrderItem> items
) {
}
