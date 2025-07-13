package com.example.ecommerce.mapper.order;

import com.example.ecommerce.dto.request.order.CreateOrderItemRequest;
import com.example.ecommerce.dto.response.order.OrderItemResponse;
import com.example.ecommerce.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItem toOrder(CreateOrderItemRequest request);

    OrderItemResponse toOrderItemResponse(OrderItem orderItem);
}
