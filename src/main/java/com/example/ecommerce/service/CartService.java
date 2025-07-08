package com.example.ecommerce.service;

import com.example.ecommerce.dto.request.cart.AddToCartRequest;
import com.example.ecommerce.dto.request.cart.UpdateCartItemRequest;
import com.example.ecommerce.dto.response.cart.CartResponse;
import com.example.ecommerce.entity.CartItem;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.ProductVariant;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.enums.ProductStatus;
import com.example.ecommerce.exception.cart.CartErrorCode;
import com.example.ecommerce.exception.cart.CartException;
import com.example.ecommerce.exception.product.ProductErrorCode;
import com.example.ecommerce.exception.product.ProductException;
import com.example.ecommerce.exception.user.UserErrorCode;
import com.example.ecommerce.exception.user.UserException;
import com.example.ecommerce.mapper.cart.CartMapper;
import com.example.ecommerce.repository.CartItemRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.ProductVariantRepository;
import com.example.ecommerce.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CartService {
    CartItemRepository cartItemRepository;
    ProductVariantRepository productVariantRepository;
    UserRepository userRepository;
    ProductRepository productRepository;
    CartMapper cartMapper;

    public CartResponse addToCart(String userId, AddToCartRequest request) {
        log.info("Adding to cart - User: {}, Product: {}, Variant: {}, Quantity: {}",
                userId, request.getProductId(), request.getProductVariantId(), request.getQuantity());

        if (userId == null) {
            throw new UserException(UserErrorCode.USER_NOT_LOGGED_IN);
        }

        // Validate user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_EXISTED));

        // Validate product exists and is active
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));

        if (product.getStatus() != ProductStatus.ACTIVE){
            throw new ProductException(ProductErrorCode.PRODUCT_NOT_AVAILABLE);
        }


        // Validate variant if specified
        ProductVariant variant = null;
        if (request.getProductVariantId() != null){
            variant = productVariantRepository.findById(request.getProductVariantId())
                    .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_VARIANT_NOT_FOUND));

            if (!variant.getProduct().getId().equals(request.getProductId())){
                throw  new ProductException(ProductErrorCode.PRODUCT_VARIANT_NOT_BELONG_TO_PRODUCT);
            }

            if (Boolean.FALSE.equals(variant.getIsActive())) {
                throw new ProductException(ProductErrorCode.PRODUCT_VARIANT_NOT_AVAILABLE);
            }
        }

        // Check stock availability
        int availableStock = getAvailableStock(product, variant);
        if (availableStock < request.getQuantity()) {
            throw new ProductException(ProductErrorCode.PRODUCT_VARIANT_INSUFFICIENT_STOCK);
        }

        // Find existing cart item
        Optional<CartItem> existingItemOpt = cartItemRepository.findByUserIdAndProductIdAndProductVariantId(
                userId, request.getProductId(), request.getProductVariantId());

        if (existingItemOpt.isPresent()) {
            // Update existing item
            CartItem existingItem = existingItemOpt.get();

            int newQuantity = existingItem.getQuantity() + request.getQuantity();
            if (availableStock < newQuantity) {
                throw new CartException(CartErrorCode.INSUFFICIENT_STOCK_NOT_COMPLETE);
            }

            existingItem.setQuantity(newQuantity);
            cartItemRepository.save(existingItem);
        } else {
            // Create new cart item
            CartItem newItem = CartItem.builder()
                    .user(user)
                    .product(product)
                    .productVariant(variant)
                    .quantity(request.getQuantity())
                    .build();

            cartItemRepository.save(newItem);
        }
        return getCart(userId);
    }

    // =============== Get Cart ===============

    public CartResponse getCart(String userId) {
        if (userId == null) {
            return CartResponse.builder()
                    .items(Collections.emptyList())
                    .totalItems(0)
                    .totalQuantity(0)
                    .subtotal(BigDecimal.ZERO)
                    .isEmpty(true)
                    .build();
        }

        List<CartItem> cartItems = cartItemRepository.findByUserIdWithProductDetails(userId);
        return cartMapper.toCarResponse(cartItems);
    }

    // ========== Update Cart Item ==========

    public CartResponse updateCartItem(String userId, UpdateCartItemRequest request) {
        if (userId == null) {
            throw new UserException(UserErrorCode.USER_NOT_LOGGED_IN);
        }

        CartItem cartItem = cartItemRepository.findById(request.getCartItemId())
                .orElseThrow(() -> new CartException(CartErrorCode.CART_ITEM_NOT_FOUND));

        // Validate ownership
        if (!cartItem.getUser().getId().equals(userId)){
            throw new CartException(CartErrorCode.CART_NOT_BELONG_TO_USER);
        }

        // Check stock availability
        int availableStock = getAvailableStock(cartItem.getProduct(), cartItem.getProductVariant());
        if (availableStock < request.getQuantity()) {
            throw new ProductException(ProductErrorCode.PRODUCT_VARIANT_INSUFFICIENT_STOCK);
        }


        cartItem.setQuantity(request.getQuantity());
        cartItemRepository.save(cartItem);

        return getCart(userId);
    }

    // ========== Remove from Cart ==========
    public CartResponse removeFromCart(String userId, Long cartItemId) {
        if (userId == null) {
            throw new UserException(UserErrorCode.USER_NOT_LOGGED_IN);
        }

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartException(CartErrorCode.CART_ITEM_NOT_FOUND));

        // Validate ownership
        if (!cartItem.getUser().getId().equals(userId)) {
            throw new CartException(CartErrorCode.CART_NOT_BELONG_TO_USER);
        }

        cartItemRepository.delete(cartItem);

        return getCart(userId);
    }

    // =============== Clear Cart ===============

    public void clearCart(String userId) {
        if (userId == null) {
            throw new UserException(UserErrorCode.USER_NOT_LOGGED_IN);
        }

        cartItemRepository.deleteByUserId(userId);
    }

    // =============== Get Cart Count ===============

    @Transactional(readOnly = true)
    public long getCartItemCount(String userId) {
        if (userId == null) {
            return 0;
        }
        return cartItemRepository.countByUserId(userId);
    }

    @Transactional(readOnly = true)
    public int getTotalQuantity(String userId) {
        if (userId == null) {
            return 0;
        }
        Integer total = cartItemRepository.getTotalQuantityByUserId(userId);
        return total != null ? total : 0;
    }


    // Helper methods
    private int getAvailableStock(Product product, ProductVariant variant) {
        if (variant != null) {
            return variant.getStockQuantity() != null ? variant.getStockQuantity() : 0;
        } else {
            return product.getStockQuantity() != null ? product.getStockQuantity() : 0;
        }
    }

}
