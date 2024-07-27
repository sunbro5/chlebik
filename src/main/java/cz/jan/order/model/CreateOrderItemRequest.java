package cz.jan.order.model;

public record CreateOrderItemRequest(
        long productId,
        long quantity
) {
}
