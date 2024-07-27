package cz.jan.order.model;

public record OrderItem(
        Long productId,
        String name,
        Long quantity,
        Double pricePerUnit
) {
}
