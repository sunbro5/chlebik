package cz.jan.order;

import cz.jan.order.model.CreateOrderItemRequest;
import cz.jan.order.model.CreateOrderRequest;
import cz.jan.order.model.Order;
import cz.jan.order.model.OrderItem;
import cz.jan.order.repository.OrderEntity;
import cz.jan.order.repository.OrderItemEntity;
import cz.jan.product.repository.ProductEntity;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.OffsetDateTime;
import java.util.Map;

@Mapper(componentModel = "spring", imports = {OffsetDateTime.class})
public interface OrderMapper {

    Order toOrder(OrderEntity orderEntity);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "name", source = "product.name")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "pricePerUnit", source = "product.pricePerUnit")
    OrderItem toOrderItem(OrderItemEntity orderItemEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "state", constant = "CREATED")
    @Mapping(target = "created", expression = "java(OffsetDateTime.now())")
    OrderEntity toCreateOrderEntity(CreateOrderRequest orderRequest, @Context Map<Long, ProductEntity> productsById);

    default OrderItemEntity toOrderItemEntity(
            CreateOrderItemRequest orderItemRequest, @Context Map<Long, ProductEntity> productsById) {
        return OrderItemEntity.builder()
                .quantity(orderItemRequest.quantity())
                .product(productsById.get(orderItemRequest.productId()))
                .build();
    }


}
