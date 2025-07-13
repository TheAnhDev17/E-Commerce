package com.example.ecommerce.mapper.order;

import com.example.ecommerce.dto.request.order.CreateOrderRequest;
import com.example.ecommerce.dto.response.order.OrderResponse;
import com.example.ecommerce.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    Order toOrder(CreateOrderRequest request);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userEmail", source = "user.email")
    OrderResponse toOrderResponse(Order order);
}
