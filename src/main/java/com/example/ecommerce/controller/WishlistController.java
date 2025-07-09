package com.example.ecommerce.controller;

import com.example.ecommerce.dto.request.wishlist.AddToWishlistRequest;
import com.example.ecommerce.dto.response.common.ApiResponse;
import com.example.ecommerce.dto.response.wishlist.WishlistResponse;
import com.example.ecommerce.service.WishlistService;
import com.nimbusds.jwt.JWT;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wishlist")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WishlistController {

    WishlistService wishlistService;

    @PostMapping
    public ApiResponse<WishlistResponse> addToWishlist(@RequestBody @Valid AddToWishlistRequest request) {
        String  userId = getUserIdFromJWT();
        return ApiResponse.<WishlistResponse>builder()
                .result(wishlistService.addToWishlist(userId, request))
                .build();
    }

    @GetMapping
    public ApiResponse<WishlistResponse> getWishlist() {
        String  userId = getUserIdFromJWT();

        return ApiResponse.<WishlistResponse>builder()
                .result(wishlistService.getWishlist(userId))
                .build();
    }

    @DeleteMapping("/products/{productId}")
    public ApiResponse<WishlistResponse> removeFromWishlist(@PathVariable Long productId) {
        String  userId = getUserIdFromJWT();

        return ApiResponse.<WishlistResponse>builder()
                .result(wishlistService.removeFromWishlist(userId,productId))
                .build();
    }

    @DeleteMapping()
    public ApiResponse<WishlistResponse> clearWishlist() {
        String  userId = getUserIdFromJWT();

        wishlistService.clearWishlist(userId);

        return ApiResponse.<WishlistResponse>builder()
                .result(null)
                .message("Clear wishlist successfully")
                .build();
    }

    @GetMapping("/check/{productId}")
    public ApiResponse<Boolean> checkProductInWishlist(@PathVariable Long productId) {
        String  userId = getUserIdFromJWT();

        return ApiResponse.<Boolean>builder()
                .result(wishlistService.isProductInWishlist(userId,productId))
                .build();
    }

    @GetMapping("/count")
    public ApiResponse<Long> getWishlistCount() {
        String  userId = getUserIdFromJWT();

        return ApiResponse.<Long>builder()
                .result(wishlistService.getWishlistCount(userId))
                .build();
    }

    private String getUserIdFromJWT() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();

        return jwt.getClaimAsString("userId");
    }
}
