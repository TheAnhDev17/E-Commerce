package com.example.ecommerce.mapper.cart;

import com.example.ecommerce.dto.response.cart.CartResponse;
import com.example.ecommerce.entity.CartItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartResponse toCartResponse(List<CartItem> cartItems);
}
