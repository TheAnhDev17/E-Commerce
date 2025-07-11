package com.example.ecommerce.dto.response.order;

import com.example.ecommerce.enums.OrderStatus;
import com.example.ecommerce.enums.PaymentMethod;
import com.example.ecommerce.enums.PaymentStatus;
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
    BigDecimal subtotal;
    BigDecimal shippingFee;
    BigDecimal discountAmount;
    BigDecimal taxAmount;
    BigDecimal totalAmount;

    PaymentMethod paymentMethod;
    PaymentStatus paymentStatus;
    String paymentTransactionId;
    LocalDateTime paymentDate;

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
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime confirmedAt;
    LocalDateTime shippedAt;
    LocalDateTime deliveredAt;
    LocalDateTime cancelledAt;

    String notes;
    String adminNotes;
    String couponCode;
    String cancellationReason;

    int totalItems;
    List<OrderItemResponse> orderItems;
}
