package cz.jan.order;

import cz.jan.order.exception.OrderProductQuantityException;
import cz.jan.order.model.CreateOrderItemRequest;
import cz.jan.order.model.CreateOrderRequest;
import cz.jan.order.model.OrderStateType;
import cz.jan.order.repository.OrderEntity;
import cz.jan.order.state.AbstractOrderStrategy;
import cz.jan.product.repository.ProductEntity;
import cz.jan.product.repository.ProductRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OrderActionFacade {

    private final OrderService orderService;
    private final ProductRepository productRepository;
    private final Map<OrderStateType, AbstractOrderStrategy> orderStrategyByType;

    public OrderActionFacade(OrderService orderService, ProductRepository productRepository,
                             List<AbstractOrderStrategy> orderStrategies) {
        this.orderService = orderService;
        this.productRepository = productRepository;
        this.orderStrategyByType = orderStrategies.stream()
                .collect(Collectors.toMap(AbstractOrderStrategy::getType, Function.identity()));
    }

    @Transactional
    public void createOrder(CreateOrderRequest orderRequest) {
        List<Long> productIds = orderRequest.items().stream()
                .map(CreateOrderItemRequest::productId)
                .toList();

        Map<Long, ProductEntity> productsToAddById = productRepository.findAllActiveWithLock(productIds).stream()
                .collect(Collectors.toMap(ProductEntity::getId, Function.identity()));

        decreaseProductQuantityForOrder(orderRequest, productsToAddById);
        orderService.createOrder(orderRequest, productsToAddById);
    }

    public void orderPayment(long orderId) {
        OrderEntity orderEntity = orderService.getOrderEntityOrThrow(orderId);
        orderStrategyByType.get(orderEntity.getState()).orderPayment(orderEntity);
    }

    public void cancelOrder(long orderId) {
        OrderEntity orderEntity = orderService.getOrderEntityOrThrow(orderId);
        orderStrategyByType.get(orderEntity.getState()).orderCancel(orderEntity);
    }

    private void decreaseProductQuantityForOrder(CreateOrderRequest orderRequest, Map<Long, ProductEntity> productsToAddById) {
        List<String> productQuantityErrors = new LinkedList<>();
        List<ProductEntity> decreasedProducts = orderRequest.items().stream()
                .map(orderItemRequest -> {
                    ProductEntity product = productsToAddById.get(orderItemRequest.productId());
                    long newProductQuantity = product.getQuantity() - orderItemRequest.quantity();
                    if (newProductQuantity < 0) {
                        String notEnoughProductQuantity = "Product " + product.getId() +
                                " does not have enough quantity, missing quantity " +
                                Math.abs(newProductQuantity);
                        productQuantityErrors.add(notEnoughProductQuantity);
                    }
                    product.setQuantity(newProductQuantity);
                    return product;
                })
                .toList();
        if (!productQuantityErrors.isEmpty()) {
            throw new OrderProductQuantityException(productQuantityErrors);
        }
        productRepository.saveAll(decreasedProducts);
    }
}
