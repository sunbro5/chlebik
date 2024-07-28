package cz.jan.order.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateOrderItemRequest(
        @NotNull
        Long productId,
        @NotNull
        @Min(1)
        Long quantity
) {
}
