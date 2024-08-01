package cz.jan.order;

import cz.jan.AbstractEshopIntegrationTest;
import cz.jan.order.model.OrderStateType;
import cz.jan.order.repository.model.OrderEntity;
import cz.jan.product.repository.ProductEntity;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
class InvalidateOrderJobTest extends AbstractEshopIntegrationTest {

    @Test()
    void invalidateOrder() {
        ProductEntity product = productRepository.findById(2000008L).orElseThrow();
        assertEquals(10L, product.getQuantity());

        OrderEntity orderEntity = orderRepository.findById(1000004L).orElseThrow();
        assertEquals(OrderStateType.CREATED, orderEntity.getState());
        orderEntity.setCreated(OffsetDateTime.now().minusMinutes(31));

        invalidateOrderJob.invalidateOrder();

        OrderEntity orderEntityAfterInvalidation = orderRepository.findById(1000004L).orElseThrow();
        assertEquals(OrderStateType.CANCELED, orderEntityAfterInvalidation.getState());

        ProductEntity productAfterInvalidation = productRepository.findById(2000008L).orElseThrow();
        assertEquals(17L, productAfterInvalidation.getQuantity());
    }
}