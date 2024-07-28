package cz.jan.order;

import cz.jan.order.model.OrderStateType;
import cz.jan.order.repository.OrderEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class InvalidateOrderJob {

    @Value("${chlebik.order.invalidate.time:30}")
    private int invalidateBeforeMinutes;

    private final OrderService orderService;

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void invalidateOrder() {
        OffsetDateTime before = OffsetDateTime.now().minusMinutes(invalidateBeforeMinutes);
        List<OrderEntity> ordersToCancel = orderService.getCreatedOrderEntityBeforeTime(before);
        if (ordersToCancel.isEmpty()) {
            return;
        }
        log.info("Going to cancel invalid orders {}", ordersToCancel.stream()
                .map(OrderEntity::getId)
                .toList());

        orderService.setOrdersState(ordersToCancel, OrderStateType.CANCELED);
    }
}
