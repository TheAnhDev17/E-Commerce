package com.example.ecommerce.controller;

import com.example.ecommerce.dto.request.cart.AddToCartRequest;
import com.example.ecommerce.dto.request.cart.UpdateCartItemRequest;
import com.example.ecommerce.dto.response.cart.CartResponse;
import com.example.ecommerce.dto.response.common.ApiResponse;
import com.example.ecommerce.service.CartService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController {

    CartService cartService;

    @PostMapping
    public ApiResponse<CartResponse> addToCart(@RequestBody @Valid AddToCartRequest request) {

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


    @PutMapping
    public ApiResponse<CartResponse> updateCartItem(@RequestBody @Valid UpdateCartItemRequest request) {

        String userId = extractUserIdFromJwt();

        return ApiResponse.<CartResponse>builder()
                .result(cartService.updateCartItem(userId, request))
                .build();
    }

    @DeleteMapping("/items/{cartItemId}")
    public ApiResponse<CartResponse> deleteCartItem(@PathVariable Long cartItemId) {

        String userId = extractUserIdFromJwt();

        return ApiResponse.<CartResponse>builder()
                .result(cartService.removeFromCart(userId, cartItemId))
                .build();
    }

    @DeleteMapping
    public ApiResponse<CartResponse> deleteCart() {

        String userId = extractUserIdFromJwt();

        cartService.clearCart(userId);

        return ApiResponse.<CartResponse>builder()
                .result(null)
                .message("Clear cart successfully")
                .build();
    }

    @GetMapping("/count")
    public ApiResponse<Map<String, Object>> getCartSummary() {

        String userId = extractUserIdFromJwt();

        long itemCount = cartService.getCartItemCount(userId);
        int totalQuantity = cartService.getTotalQuantity(userId);

        Map<String, Object> summary = Map.of(
                "itemCount", itemCount,
                "totalQuantity", totalQuantity
        );

        return ApiResponse.<Map<String, Object>>builder()
                .result(summary)
                .message("Clear cart successfully")
                .build();
    }

    private String extractUserIdFromJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();

        return jwt.getClaimAsString("userId");
    }
}
