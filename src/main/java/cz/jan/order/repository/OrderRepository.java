package cz.jan.order.repository;

import cz.jan.order.model.OrderStateType;
import cz.jan.order.repository.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByStateEqualsAndCreatedLessThan(OrderStateType orderStateType, OffsetDateTime fromDateTime);
}
