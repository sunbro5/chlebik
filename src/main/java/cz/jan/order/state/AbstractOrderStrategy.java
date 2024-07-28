package cz.jan.order.state;

import cz.jan.order.OrderService;
import cz.jan.order.model.OrderStateType;
import cz.jan.order.repository.OrderEntity;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractOrderStrategy {

    protected final OrderService orderService;

    protected AbstractOrderStrategy(OrderService orderService) {
        this.orderService = orderService;
    }

    public abstract OrderStateType getType();

    @Transactional
    public abstract void orderPayment(OrderEntity orderEntity);

    @Transactional
    public abstract void orderCancel(OrderEntity orderEntity);

}
