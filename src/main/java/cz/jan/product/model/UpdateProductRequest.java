package cz.jan.product.model;


import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record UpdateProductRequest(
        String name,
        Long quantity,
        BigDecimal pricePerUnit
) {
}
