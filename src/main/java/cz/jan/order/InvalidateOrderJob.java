package cz.jan.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class InvalidateOrderJob {

    private final OrderActionDelegator orderActionDelegator;

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void invalidateOrder() {
        orderActionDelegator.invalidateOldOrders();
    }
}
