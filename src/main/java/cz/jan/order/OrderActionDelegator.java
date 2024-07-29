package cz.jan.order;

import cz.jan.order.exception.OrderProductQuantityException;
import cz.jan.order.model.CreateOrderItemRequest;
import cz.jan.order.model.CreateOrderRequest;
import cz.jan.order.model.Order;
import cz.jan.order.model.OrderStateType;
import cz.jan.order.repository.model.OrderEntity;
import cz.jan.order.repository.model.OrderItemEntity;
import cz.jan.order.strategy.AbstractOrderStrategy;
import cz.jan.product.repository.ProductEntity;
import cz.jan.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class OrderActionDelegator {

    private final OrderService orderService;
    private final OrderActionService orderActionService;
    private final Map<OrderStateType, AbstractOrderStrategy> orderStrategyByType;

    public OrderActionDelegator(OrderService orderService,
                                OrderActionService orderActionService,
                                List<AbstractOrderStrategy> orderStrategies) {
        this.orderService = orderService;
        this.orderActionService = orderActionService;
        this.orderStrategyByType = orderStrategies.stream()
                .collect(Collectors.toMap(AbstractOrderStrategy::getType, Function.identity()));
    }

    @Retryable(retryFor = OptimisticLockingFailureException.class, maxAttempts = 20,
            backoff = @Backoff(delay = 5, multiplier = 2, random = true))
    public Order createOrder(CreateOrderRequest orderRequest) {
        return orderActionService.createOrderAndTakeProducts(orderRequest);
    }

    public void orderPayment(long orderId) {
        OrderEntity orderEntity = orderService.getOrderEntityOrThrow(orderId);
        orderStrategyByType.get(orderEntity.getState()).orderPayment(orderEntity);
    }

    @Retryable(retryFor = OptimisticLockingFailureException.class, maxAttempts = 20,
            backoff = @Backoff(delay = 5, multiplier = 2, random = true))
    public void cancelOrder(long orderId) {
        OrderEntity orderEntity = orderService.getOrderEntityOrThrow(orderId);
        orderStrategyByType.get(orderEntity.getState()).orderCancel(orderEntity);
    }

    @Retryable(retryFor = OptimisticLockingFailureException.class, maxAttempts = 20,
            backoff = @Backoff(delay = 5, multiplier = 2, random = true))
    public void invalidateOldOrders() {
        orderActionService.cancelOrdersAndReleaseProducts();
    }

}
