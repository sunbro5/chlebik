package cz.jan.order.strategy;

import cz.jan.order.OrderActionService;
import cz.jan.order.OrderService;
import cz.jan.order.exception.OrderInvalidActionException;
import cz.jan.order.model.OrderStateType;
import cz.jan.order.repository.model.OrderEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderCanceledStrategy extends AbstractOrderStrategy {


    protected OrderCanceledStrategy(OrderService orderService, OrderActionService orderActionService) {
        super(orderService, orderActionService);
    }

    @Override
    public OrderStateType getType() {
        return OrderStateType.CANCELED;
    }

    @Override
    public void orderPayment(OrderEntity orderEntity) {
        throw new OrderInvalidActionException("Cannot do payment for canceled order");
    }

    @Override
    public void orderCancel(OrderEntity orderEntity) {
        throw new OrderInvalidActionException("Order is already canceled");
    }

}
