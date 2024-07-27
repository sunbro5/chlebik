package cz.jan.product.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateProductRequest(
        @NotBlank
        String name,
        @NotNull
        Long quantity,
        @NotNull
        Double pricePerUnit
) {
}
