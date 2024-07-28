package cz.jan.order;

import cz.jan.order.exception.OrderNotFoundException;
import cz.jan.order.model.CreateOrderRequest;
import cz.jan.order.model.Order;
import cz.jan.order.model.OrderStateType;
import cz.jan.order.repository.OrderEntity;
import cz.jan.order.repository.OrderRepository;
import cz.jan.product.repository.ProductEntity;
import cz.jan.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public List<Order> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toOrder)
                .toList();
    }

    public Optional<Order> getOrder(long orderId) {
        return orderRepository.findById(orderId)
                .map(orderMapper::toOrder);
    }

    public List<OrderEntity> getCreatedOrderEntityBeforeTime(OffsetDateTime from) {
        return orderRepository.findByStateEqualsAndCreatedLessThan(OrderStateType.CREATED, from);
    }

    public OrderEntity getOrderEntityOrThrow(long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    public void createOrder(CreateOrderRequest orderRequest, Map<Long, ProductEntity> productsToAddById) {
        OrderEntity orderToSave = orderMapper.toCreateOrderEntity(orderRequest, productsToAddById);
        OrderEntity savedOrder = orderRepository.save(orderToSave);
        log.info("Created order {}", savedOrder);
    }

    public void setOrderState(OrderEntity orderEntity, OrderStateType orderState) {
        orderEntity.setState(orderState);
        orderRepository.save(orderEntity);
        log.info("Changed orderId {} to state {}", orderEntity.getId(), orderState);
    }

    public void setOrdersState(List<OrderEntity> orderEntities, OrderStateType orderState) {
        orderEntities.forEach(orderEntity -> orderEntity.setState(orderState));
        orderRepository.saveAll(orderEntities);
        log.info("Changed orders with id {} to state {}", orderEntities.stream()
                .map(OrderEntity::getId)
                .toList(), orderState);
    }

}
