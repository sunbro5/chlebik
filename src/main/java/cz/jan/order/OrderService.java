package cz.jan.order;

import cz.jan.order.model.CreateOrderRequest;
import cz.jan.order.model.Order;
import cz.jan.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public void createOrder(CreateOrderRequest orderRequest) {

    }

    public void orderPayment(long orderId) {
        //TODO call payment service and check ok

    }

    public void cancelOrder(long orderId) {
    }
}
