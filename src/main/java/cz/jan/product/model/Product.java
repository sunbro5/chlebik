package cz.jan.product.model;

public record Product(
        Long id,
        String name,
        Long quantity,
        Double pricePerUnit,
        Boolean active
) {
}
