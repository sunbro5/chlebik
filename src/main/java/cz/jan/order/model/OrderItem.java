package cz.jan.order.model;

import java.math.BigDecimal;

public record OrderItem(
        Long productId,
        String name,
        Long quantity,
        BigDecimal pricePerUnit
) {
}
