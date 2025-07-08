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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    private String getUserIdFromJWT() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();

        return jwt.getClaimAsString("userId");
    }
}
