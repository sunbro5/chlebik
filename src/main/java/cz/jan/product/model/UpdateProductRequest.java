package cz.jan.product.model;


public record UpdateProductRequest(
        String name,
        Long quantity,
        Double pricePerUnit
) {
}
