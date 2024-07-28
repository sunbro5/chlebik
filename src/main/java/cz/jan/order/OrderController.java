package cz.jan.order;

import cz.jan.order.model.CreateOrderRequest;
import cz.jan.order.model.Order;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/order")
@RestController
public class OrderController {

    private final OrderActionFacade orderActionFacade;
    private final OrderService orderService;

    @GetMapping
    public List<Order> getOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable long orderId) {
        return ResponseEntity.of(orderService.getOrder(orderId));
    }

    @PostMapping
    public void createOrder(@RequestBody @Valid CreateOrderRequest orderRequest) {
        orderActionFacade.createOrder(orderRequest);
    }

    @PostMapping("/{orderId}/payment")
    public void orderPayment(@PathVariable long orderId) {
        orderActionFacade.orderPayment(orderId);
    }

    @PostMapping("/{orderId}/cancel")
    public void cancelOrder(@PathVariable long orderId) {
        orderActionFacade.cancelOrder(orderId);
    }


}
