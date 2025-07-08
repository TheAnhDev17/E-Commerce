package com.example.ecommerce.service;

import com.example.ecommerce.dto.request.wishlist.AddToWishlistRequest;
import com.example.ecommerce.dto.response.wishlist.WishlistResponse;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.entity.Wishlist;
import com.example.ecommerce.exception.product.ProductErrorCode;
import com.example.ecommerce.exception.product.ProductException;
import com.example.ecommerce.exception.user.UserErrorCode;
import com.example.ecommerce.exception.user.UserException;
import com.example.ecommerce.exception.wishlist.WishlistErrorCode;
import com.example.ecommerce.exception.wishlist.WishlistException;
import com.example.ecommerce.mapper.WishlistMapper;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.repository.WishlistRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WishlistService {
    WishlistRepository wishlistRepository;
    ProductRepository productRepository;
    UserRepository userRepository;
    WishlistMapper wishlistMapper;

    public WishlistResponse addToWishlist(String userId, AddToWishlistRequest request) {
        log.info("Adding to wishlist - User: {}, Product: {}", userId, request.getProductId());

        if (userId == null) {
            throw new WishlistException(WishlistErrorCode.USER_NOT_LOGGED_IN_TO_USE_WISHLIST);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_EXISTED));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));

        // Check if already in wishlist
        if (wishlistRepository.existsByUserIdAndProductId(user.getId(), product.getId())) {
            throw new WishlistException(WishlistErrorCode.WISHLIST_ALREADY_HAS_THIS_PRODUCT);
        }

        Wishlist wishlistItem = Wishlist.builder()
                .product(product)
                .user(user)
                .build();

        wishlistRepository.save(wishlistItem);

        return getWishlist(userId);
    }

    // =============== Get Wishlist ===============

    public WishlistResponse getWishlist(String userId) {
        if (userId == null) {
            return WishlistResponse.builder()
                    .items(Collections.emptyList())
                    .totalItems(0)
                    .isEmpty(true)
                    .build();
        }

        List<Wishlist> wishlistItems = wishlistRepository.findByUserIdWithProductDetails(userId);
        return wishlistMapper.toWishlistResponse(wishlistItems);
    }
}
