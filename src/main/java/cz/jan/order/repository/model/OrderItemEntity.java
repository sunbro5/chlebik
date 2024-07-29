package cz.jan.order.repository.model;

import cz.jan.product.repository.ProductEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="ORDER_ITEM")
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "ORDER_ID")
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private ProductEntity product;

    @Column(name="QUANTITY", nullable=false)
    private Long quantity;

}
