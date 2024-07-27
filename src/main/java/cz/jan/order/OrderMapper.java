package cz.jan.order;

import cz.jan.order.model.CreateOrderRequest;
import cz.jan.order.model.Order;
import cz.jan.order.model.OrderItem;
import cz.jan.order.repository.OrderEntity;
import cz.jan.order.repository.OrderItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    Order toOrder(OrderEntity orderEntity);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "name", source = "product.name")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "pricePerUnit", source = "product.pricePerUnit")
    OrderItem toOrderItem(OrderItemEntity orderItemEntity);

    @Mapping(target = "id", ignore = true)
    OrderEntity toOrderItem(CreateOrderRequest orderRequest);

}
