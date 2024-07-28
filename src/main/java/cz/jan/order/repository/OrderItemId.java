package cz.jan.order.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class OrderItemId implements Serializable {

    @Column(name = "ORDER_ID")
    private Long orderId;
    @Column(name = "PRODUCT_ID")
    private Long productId;
}
