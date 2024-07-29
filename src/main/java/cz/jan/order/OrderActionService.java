package cz.jan.order;

import cz.jan.order.exception.OrderProductQuantityException;
import cz.jan.order.model.CreateOrderItemRequest;
import cz.jan.order.model.CreateOrderRequest;
import cz.jan.order.model.Order;
import cz.jan.order.model.OrderStateType;
import cz.jan.order.repository.model.OrderEntity;
import cz.jan.order.repository.model.OrderItemEntity;
import cz.jan.product.repository.ProductEntity;
import cz.jan.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderActionService {


    @Value("${chlebik.order.invalidate.time:30}")
    private int invalidateBeforeMinutes;
    private final OrderService orderService;
    private final ProductRepository productRepository;

    public Order createOrderAndTakeProducts(CreateOrderRequest orderRequest) {
        List<Long> productIds = orderRequest.items().stream()
                .map(CreateOrderItemRequest::productId)
                .toList();

        Map<Long, ProductEntity> productsToAddById = productRepository.findAllActiveWithLock(productIds).stream()
                .collect(Collectors.toMap(ProductEntity::getId, Function.identity()));

        decreaseProductQuantityForOrder(orderRequest, productsToAddById);
        return orderService.createOrder(orderRequest, productsToAddById);
    }

    public void cancelOrderAndReleaseProducts( OrderEntity orderEntity) {
        releaseProductsQuantity(orderEntity);
        orderService.setOrderState(orderEntity, OrderStateType.CANCELED);
    }

    public void cancelOrdersAndReleaseProducts() {
        OffsetDateTime before = OffsetDateTime.now().minusMinutes(invalidateBeforeMinutes);
        List<OrderEntity> ordersToCancel = orderService.getCreatedOrderEntityBeforeTime(before);
        if (ordersToCancel.isEmpty()) {
            return;
        }
        log.info("Going to cancel invalid orders {}", ordersToCancel.stream()
                .map(OrderEntity::getId)
                .toList());

        releaseProductsQuantity(ordersToCancel);
        orderService.setOrdersState(ordersToCancel, OrderStateType.CANCELED);
    }

    private void decreaseProductQuantityForOrder(CreateOrderRequest orderRequest, Map<Long, ProductEntity> productsToAddById) {
        List<String> productQuantityErrors = new LinkedList<>();
        orderRequest.items().forEach(orderItemRequest -> {
            ProductEntity product = productsToAddById.get(orderItemRequest.productId());
            long newProductQuantity = product.getQuantity() - orderItemRequest.quantity();
            if (newProductQuantity < 0) {
                String notEnoughProductQuantity = "Product " + product.getId() +
                        " does not have enough quantity, missing quantity " +
                        Math.abs(newProductQuantity);
                productQuantityErrors.add(notEnoughProductQuantity);
            }
            product.setQuantity(newProductQuantity);
            log.info("Decreased product {} to quantity {}", product.getId(), product.getQuantity());
            ProductEntity savedProduct = productRepository.save(product);
            productsToAddById.put(orderItemRequest.productId(), savedProduct);
        });
        if (!productQuantityErrors.isEmpty()) {
            throw new OrderProductQuantityException(productQuantityErrors);
        }
    }

    private void releaseProductsQuantity(List<OrderEntity> ordersToCancel) {
        Set<Long> productIds = ordersToCancel.stream()
                .flatMap(orderEntity -> orderEntity.getItems().stream())
                .map(OrderItemEntity::getProduct)
                .map(ProductEntity::getId)
                .collect(Collectors.toSet());

        Map<Long, ProductEntity> productsToReleaseQuantity = productRepository.findAllWithLock(productIds).stream()
                .collect(Collectors.toMap(ProductEntity::getId, Function.identity()));

        ordersToCancel.stream()
                .flatMap(orderEntity -> orderEntity.getItems().stream())
                .forEach(orderItemEntity -> releaseProduct(orderItemEntity, productsToReleaseQuantity));
    }

    private void releaseProductsQuantity(OrderEntity orderToCancel) {
        Set<Long> productIds = orderToCancel.getItems().stream()
                .map(OrderItemEntity::getProduct)
                .map(ProductEntity::getId)
                .collect(Collectors.toSet());

        Map<Long, ProductEntity> productsToReleaseQuantity = productRepository.findAllWithLock(productIds).stream()
                .collect(Collectors.toMap(ProductEntity::getId, Function.identity()));

        orderToCancel.getItems()
                .forEach(orderItemEntity -> releaseProduct(orderItemEntity, productsToReleaseQuantity));
    }

    private void releaseProduct(OrderItemEntity orderItemEntity, Map<Long, ProductEntity> productsToReleaseQuantity){
        ProductEntity product = productsToReleaseQuantity.get(orderItemEntity.getProduct().getId());
        long newProductQuantity = product.getQuantity() + orderItemEntity.getQuantity();
        product.setQuantity(newProductQuantity);
        log.info("Release product {} to quantity {}", product.getId(), product.getQuantity());
        productRepository.save(product);
    }
}
