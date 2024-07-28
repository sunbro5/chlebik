package cz.jan.product.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateProductRequest(
        @NotBlank
        String name,
        @NotNull
        Long quantity,
        @Positive
        @NotNull
        Double pricePerUnit
) {
}
