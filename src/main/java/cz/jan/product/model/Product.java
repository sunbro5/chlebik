package cz.jan.product.model;

import java.math.BigDecimal;

public record Product(
        Long id,
        String name,
        Long quantity,
        BigDecimal pricePerUnit,
        Boolean active
) {
}
