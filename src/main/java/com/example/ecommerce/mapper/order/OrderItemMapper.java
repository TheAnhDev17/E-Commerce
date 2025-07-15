package com.example.ecommerce.mapper.order;

import com.example.ecommerce.dto.request.order.CreateOrderItemRequest;
import com.example.ecommerce.dto.response.order.OrderItemResponse;
import com.example.ecommerce.entity.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItem toOrderItem(CreateOrderItemRequest request);

    OrderItemResponse toOrderItemResponse(OrderItem orderItem);
}
