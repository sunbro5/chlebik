package cz.jan.order;

import cz.jan.AbstractChlebikIntegrationTest;
import cz.jan.common.error.ApiError;
import cz.jan.order.model.CreateOrderItemRequest;
import cz.jan.order.model.CreateOrderRequest;
import cz.jan.order.model.Order;
import cz.jan.order.model.OrderStateType;
import cz.jan.product.repository.ProductEntity;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderControllerTest extends AbstractChlebikIntegrationTest {

    @Test
    void givenOrderOneItem_whenCallEndpoint_shouldSaveOrder() throws Exception {
        CreateOrderRequest orderRequest = CreateOrderRequest.builder()
                .items(List.of(CreateOrderItemRequest.builder()
                        .productId(2000001L)
                        .quantity(1L)
                        .build()))
                .build();
        Order order = callPostWithBody("/api/order", orderRequest, Order.class);

        assertTrue(orderRepository.findById(order.orderId()).isPresent());
    }

    @Test
    void givenProductQuantityFifty_whenCallCreateOrderFiftyTimes_shouldProcessItWithRemainingZero() throws ExecutionException, InterruptedException {
        CreateOrderRequest orderRequest = CreateOrderRequest.builder()
                .items(List.of(CreateOrderItemRequest.builder()
                        .productId(2000007L)
                        .quantity(1L)
                        .build()))
                .build();

        List<Callable<Void>> tasks = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            tasks.add(() -> {
                callCreateOrder(orderRequest);
                return null;
            });
        }

        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<Void>> futures = executorService.invokeAll(tasks);
            for (Future<Void> future : futures) {
                future.get();
            }
        }

        assertEquals(0, productRepository.findById(2000007L).orElseThrow().getQuantity());
    }

    private void callCreateOrder(CreateOrderRequest orderRequest) {
        try {
            callPostWithBody("/api/order", orderRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void givenProductQuantityLow_whenCallCreateOrder_shouldReturnBadRequestWithMissingQuantity() throws Exception {
        CreateOrderRequest orderRequest = CreateOrderRequest.builder()
                .items(List.of(CreateOrderItemRequest.builder()
                                .productId(2000002L)
                                .quantity(5L)
                                .build(),
                        CreateOrderItemRequest.builder()
                                .productId(2000004L)
                                .quantity(20L)
                                .build())
                )
                .build();
        ApiError order = callPostWithBodyBadRequest("/api/order", orderRequest, ApiError.class);

        assertEquals(2, order.errors().size());
        assertEquals("Product 2000002 does not have enough quantity, missing quantity 4", order.errors().get(0));
        assertEquals("Product 2000004 does not have enough quantity, missing quantity 10", order.errors().get(1));
    }

    @Test
    void givenOrderCanceled_whenCallEndpointPayment_shouldReturnBadRequest() throws Exception {
        callPost("/api/order/1000003/payment")
                .andExpect(status().isBadRequest());
    }

    @Test
    void cancelOrder() throws Exception {
        long productId = 2000009;

        Optional<ProductEntity> product = productRepository.findById(productId);
        assertTrue(product.isPresent());
        assertEquals(10, product.get().getQuantity());

        callPost("/api/order/1000005/cancel")
                .andExpect(status().isOk());
        assertEquals(OrderStateType.CANCELED, orderRepository.findById(1000005L).orElseThrow().getState());
        product = productRepository.findById(productId);
        assertTrue(product.isPresent());
        assertEquals(11, product.get().getQuantity());
    }
}