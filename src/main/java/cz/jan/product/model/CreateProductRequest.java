package cz.jan.product.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CreateProductRequest(
        @NotBlank
        String name,
        @NotNull
        Long quantity,
        @DecimalMin("0")
        @NotNull
        BigDecimal pricePerUnit
) {
}
