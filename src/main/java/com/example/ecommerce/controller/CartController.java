package com.example.ecommerce.controller;

import com.example.ecommerce.dto.request.cart.AddToCartRequest;
import com.example.ecommerce.dto.response.cart.CartResponse;
import com.example.ecommerce.dto.response.common.ApiResponse;
import com.example.ecommerce.service.CartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController {

    CartService cartService;

    @PostMapping
    public ApiResponse<CartResponse> addToCart(@RequestBody AddToCartRequest request) {

        String userId = extractUserIdFromJwt();

        return ApiResponse.<CartResponse>builder()
                .result(cartService.addToCart(userId, request))
                .build();
    }

    @GetMapping
    public ApiResponse<CartResponse> getCart() {

        String userId = extractUserIdFromJwt();

        return ApiResponse.<CartResponse>builder()
                .result(cartService.getCart(userId))
                .build();
    }

    private String extractUserIdFromJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();

        return jwt.getClaimAsString("userId");
    }
}
