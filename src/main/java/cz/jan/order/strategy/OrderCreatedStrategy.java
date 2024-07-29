package cz.jan.order.strategy;

import cz.jan.order.OrderActionService;
import cz.jan.order.OrderService;
import cz.jan.order.exception.OrderInvalidActionException;
import cz.jan.order.model.OrderStateType;
import cz.jan.order.repository.model.OrderEntity;
import cz.jan.order.repository.model.OrderItemEntity;
import cz.jan.product.repository.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedStrategy extends AbstractOrderStrategy {


    protected OrderCreatedStrategy(OrderService orderService, OrderActionService orderActionService) {
        super(orderService, orderActionService);
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
        orderActionService.cancelOrderAndReleaseProducts(orderEntity);
    }

    private void checkOrderItemsStillActive(OrderEntity orderEntity) {
        boolean allItemsActive = orderEntity.getItems().stream()
                .map(OrderItemEntity::getProduct)
                .allMatch(ProductEntity::getActive);
        if (!allItemsActive) {
            orderService.setOrderState(orderEntity, OrderStateType.CANCELED);
            throw new OrderInvalidActionException("Items in order are no longer active, order is canceled");
        }
    }

}
