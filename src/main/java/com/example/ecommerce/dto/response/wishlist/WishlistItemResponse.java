package com.example.ecommerce.dto.response.wishlist;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WishlistItemResponse {
    Long id;
    Long productId;
    String productName;
    String productSlug;
    BigDecimal productPrice;
    BigDecimal productComparePrice;
    String productImageUrl;
    Boolean isInStock;
    Boolean isOnSale;
    Boolean hasVariants;
    LocalDateTime addedAt;
}
