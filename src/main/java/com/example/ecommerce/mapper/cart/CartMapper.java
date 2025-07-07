package com.example.ecommerce.mapper.cart;

import com.example.ecommerce.dto.request.cart.AddToCartRequest;
import com.example.ecommerce.dto.response.cart.CartItemResponse;
import com.example.ecommerce.dto.response.cart.CartResponse;
import com.example.ecommerce.entity.CartItem;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.ProductImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {

    CartItem toCartItem(AddToCartRequest request);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productSlug", source = "product.slug")
    @Mapping(target = "productPrice", source = "product.price")
    @Mapping(target = "productComparePrice", source = "product.comparePrice")
    @Mapping(target = "productImageUrl", expression = "java(getFirstProductImage(cartItem.getProduct()))")
    @Mapping(target = "productVariantId", source = "productVariant.id")
    @Mapping(target = "productVariantName", source = "productVariant.nameSuffix")
    @Mapping(target = "productVariantSku", source = "productVariant.sku")
    @Mapping(target = "productVariantPrice", source = "productVariant.price")
    @Mapping(target = "productVariantAttributes", source = "productVariant.attributes")
    @Mapping(target = "itemTotal", expression = "java(calculateItemTotal(cartItem))")
    @Mapping(target = "isInStock", expression = "java(checkStock(cartItem))")
    @Mapping(target = "availableStock", source = "productVariant.stockQuantity")
    @Mapping(target = "addedAt", source = "createdAt")
    CartItemResponse toCartItemResponse(CartItem cartItem);

    List<CartItemResponse> toCartItemResponses(List<CartItem> cartItems);

    default CartResponse toCarResponse(List<CartItem> cartItems) {
        if (cartItems == null) {
            return CartResponse.builder()
                    .items(Collections.emptyList())
                    .totalItems(0)
                    .totalQuantity(0)
                    .subtotal(BigDecimal.ZERO)
                    .lastUpdated(LocalDateTime.now())
                    .isEmpty(true)
                    .build();
        }

        List<CartItemResponse> itemResponses = toCartItemResponses(cartItems);

        return CartResponse.builder()
                .items(itemResponses)
                .totalItems(cartItems.size())
                .totalQuantity(cartItems.stream().mapToInt(CartItem::getQuantity).sum())
                .subtotal(cartItems.stream()
                        .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                )
                .lastUpdated(LocalDateTime.now())
                .isEmpty(cartItems.isEmpty())
                .build();
    }

    default String getFirstProductImage(Product product) {
        if (product == null || product.getImages() == null || product.getImages().isEmpty()) {
            return null;
        }

        // Lấy image đầu tiên từ Set
        return product.getImages().stream()
                .findFirst()
                .map(ProductImage::getImageUrl)
                .orElse(null);
    }

    default BigDecimal calculateItemTotal(CartItem cartItem) {
        if (cartItem.getProductVariant() != null && cartItem.getProductVariant().getPrice() != null) {
            return cartItem.getProductVariant().getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity()));
        }
        return BigDecimal.ZERO;
    }

    default Boolean checkStock(CartItem cartItem) {
        if (cartItem.getProductVariant() != null) {
            return cartItem.getProductVariant().getStockQuantity() >= cartItem.getQuantity();
        }
        return false;
    }
}
