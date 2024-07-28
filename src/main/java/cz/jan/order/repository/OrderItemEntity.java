package cz.jan.order.repository;

import cz.jan.product.repository.ProductEntity;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Entity
@Table(name="ORDER_ITEM")
public class OrderItemEntity {

    @EmbeddedId
    private OrderItemId id;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "ORDER_ID", nullable = false)
    private OrderEntity order;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private ProductEntity product;

    @Column(name="QUANTITY", nullable=false)
    private long quantity;

}
