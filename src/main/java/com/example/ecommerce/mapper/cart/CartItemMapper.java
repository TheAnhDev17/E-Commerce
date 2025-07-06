package com.example.ecommerce.mapper.cart;

import com.example.ecommerce.dto.request.cart.AddToCartRequest;
import com.example.ecommerce.dto.response.cart.CartItemResponse;
import com.example.ecommerce.entity.CartItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    CartItem toCartItem(AddToCartRequest request);

    CartItemResponse toCartItemResponse(CartItem cartItem);
}
