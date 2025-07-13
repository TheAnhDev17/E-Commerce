package com.example.ecommerce.dto.response.order;

import com.example.ecommerce.enums.OrderItemStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemResponse {
    Long id;

    // Product info (snapshot)
    Long productId;
    String productName;
    String productSku;
    String productImageUrl;

    // Variant info
    Long productVariantId;
    String variantName;
    String variantSku;

    // Pricing
    BigDecimal unitPrice;
    BigDecimal originalPrice;
    BigDecimal discountAmount;
    Integer quantity;
    BigDecimal subtotal;

    // Status
    OrderItemStatus status;
    String statusDescription;
    String trackingNumber;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime shippedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime deliveredAt;

    // Review info
    boolean canBeReviewed;
    boolean hasReview;
    Integer reviewRating;

    // Helper fields
    String fullProductName; // product + variant name
    String displaySku;
    BigDecimal totalDiscount;
    boolean canBeReturned;
}
