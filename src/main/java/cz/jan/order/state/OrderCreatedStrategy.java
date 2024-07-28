package cz.jan.order.state;

import cz.jan.order.OrderService;
import cz.jan.order.exception.OrderInvalidActionException;
import cz.jan.order.model.OrderStateType;
import cz.jan.order.repository.OrderEntity;
import cz.jan.order.repository.OrderItemEntity;
import cz.jan.product.repository.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedStrategy extends AbstractOrderStrategy {

    protected OrderCreatedStrategy(OrderService orderService) {
        super(orderService);
    }

    @Override
    public OrderStateType getType() {
        return OrderStateType.CREATED;
    }

    @Override
    public void orderPayment(OrderEntity orderEntity) {
        //TODO call payment service and check ok
        checkOrderItemsStillActive(orderEntity);
        orderService.setOrderState(orderEntity, OrderStateType.PAYED);
    }

    @Override
    public void orderCancel(OrderEntity orderEntity) {
        orderService.setOrderState(orderEntity, OrderStateType.CANCELED);
    }

    private void checkOrderItemsStillActive(OrderEntity orderEntity) {
        boolean allItemsActive = orderEntity.getItems().stream()
                .map(OrderItemEntity::getProduct)
                .allMatch(ProductEntity::isActive);
        if (!allItemsActive) {
            orderService.setOrderState(orderEntity, OrderStateType.CANCELED);
            throw new OrderInvalidActionException("Items in order are no longer active, order is canceled");
        }
    }

}
