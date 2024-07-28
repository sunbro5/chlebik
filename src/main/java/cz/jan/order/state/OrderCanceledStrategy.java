package cz.jan.order.state;

import cz.jan.order.OrderService;
import cz.jan.order.exception.OrderInvalidActionException;
import cz.jan.order.model.OrderStateType;
import cz.jan.order.repository.OrderEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderCanceledStrategy extends AbstractOrderStrategy {

    protected OrderCanceledStrategy(OrderService orderService) {
        super(orderService);
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
