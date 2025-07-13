package com.example.ecommerce.dto.response.order;

import com.example.ecommerce.enums.OrderStatus;
import com.example.ecommerce.enums.PaymentMethod;
import com.example.ecommerce.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    Long id;
    String orderNumber; //ORD-20250711-0001
    OrderStatus orderStatus;

    // Pricing
    BigDecimal subtotal;
    BigDecimal shippingFee;
    BigDecimal discountAmount;
    BigDecimal taxAmount;
    BigDecimal totalAmount;

    // User info
    String userId;
    String userEmail;

    // Payment
    PaymentMethod paymentMethod;
    PaymentStatus paymentStatus;
    String paymentTransactionId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime paymentDate;

    // Shipping
    String shippingAddress;
    String shippingCity;
    String shippingDistrict;
    String shippingWard;
    String shippingPostalCode;
    String recipientName;
    String recipientPhone;
    String recipientEmail;

    // Tracking
    String trackingNumber;
    String carrier;

    // Date
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime updatedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime confirmedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime shippedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime deliveredAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime cancelledAt;

    // Additional info
    String notes;
    String adminNotes;
    String couponCode;
    String cancellationReason;
    int totalItems;

    // Order items
    List<OrderItemResponse> orderItems;
}
