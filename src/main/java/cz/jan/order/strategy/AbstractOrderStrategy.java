package cz.jan.order.strategy;

import cz.jan.order.OrderActionDelegator;
import cz.jan.order.OrderActionService;
import cz.jan.order.OrderService;
import cz.jan.order.model.OrderStateType;
import cz.jan.order.repository.model.OrderEntity;

public abstract class AbstractOrderStrategy {

    protected final OrderService orderService;
    protected final OrderActionService orderActionService;

    protected AbstractOrderStrategy(OrderService orderService, OrderActionService orderActionService) {
        this.orderService = orderService;
        this.orderActionService = orderActionService;
    }

    public abstract OrderStateType getType();

    public abstract void orderPayment(OrderEntity orderEntity);

    public abstract void orderCancel(OrderEntity orderEntity);

}
