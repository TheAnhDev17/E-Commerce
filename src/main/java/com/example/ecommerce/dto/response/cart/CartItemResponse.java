package com.example.ecommerce.dto.response.cart;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemResponse {
    Long id;
    Long productId;
    String productName;
    String productSlug;
    BigDecimal productPrice;
    BigDecimal productComparePrice;
    String productImageUrl;
    Long productVariantId;
    String productVariantName;
    String productVariantSku;
    BigDecimal productVariantPrice;
    Map<String, Object> productVariantAttributes;
    Integer quantity;
    BigDecimal itemTotal;
    Boolean isInStock;
    Integer availableStock;
    LocalDateTime addedAt;
}
