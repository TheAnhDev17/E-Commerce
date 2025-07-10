package com.example.ecommerce.entity;

import com.example.ecommerce.enums.OrderStatus;
import com.example.ecommerce.enums.PaymentMethod;
import com.example.ecommerce.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;


//    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
//    List<OrderItem> orderItems;


    // Order information
    @Column(name = "order_number", unique = true, nullable = false)
    String orderNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    OrderStatus status = OrderStatus.PENDING;

    // Pricing
    @Column(name = "subtotal", precision = 10, scale = 2, nullable = false)
    BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "shipping_fee", precision = 10, scale = 2, nullable = false)
    BigDecimal shippingFee = BigDecimal.ZERO;

    @Column(name = "discount_amount", precision = 10, scale = 2, nullable = false)
    BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "tax_amount", precision = 10, scale = 2, nullable = false)
    BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "total_amount", precision = 10, scale = 2, nullable = false)
    BigDecimal totalAmount = BigDecimal.ZERO;

    // Payment
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    PaymentStatus paymentStatus;

    @Column(name = "payment_transaction_id")
    String paymentTransactionId; // Mã giao dịch thanh toán từ bên thứ 3 (unique)

    @Column(name = "payment_date")
    LocalDateTime paymentDate;


    // Shipping information
    @Column(name = "shipping_address", nullable = false, length = 500)
    String shippingAddress;

    @Column(name = "shipping_city", nullable = false, length = 100)
    String shippingCity;

    @Column(name = "shipping_district", length = 100)
    String shippingDistrict;

    @Column(name = "shipping_ward")
    String shippingWard;

    @Column(name = "shipping_postal_code")
    String shippingPostalCode;

    @Column(name = "recipient_name", nullable = false)
    String recipientName;

    @Column(name = "recipient_phone", nullable = false)
    String recipientPhone;

    @Column(name = "recipient_email")
    String recipientEmail;

    // Tracking
    @Column(name = "tracking_number")
    String trackingNumber; // mã vận đơn

    @Column(name = "carrier")
    String carrier; // GHN, GHTK, J&T, Viettel Post...

    // Dates
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Column(name = "confirmed_at")
    LocalDateTime confirmedAt;

    @Column(name = "shipped_at")
    LocalDateTime shippedAt;

    @Column(name = "delivered_at")
    LocalDateTime deliveredAt;

    @Column(name = "cancelled_at")
    LocalDateTime cancelled_at;

    // Additional fields
    @Column(name = "notes", length = 1000)
    String notes;  // ghi chú từ khách hàng

    @Column(name = "admin_notes", length = 1000)
    String adminNotes; // ghi chú nội bộ

    @Column(name = "coupon_code", length = 50)
    String couponCode;

    @Column(name = "cancellation_reason", length = 500)
    String cancellationReason;

    // Calculated methods

    public void calculateTotalAmount() {
        this.totalAmount = this.subtotal
                .add(this.shippingFee)
                .add(this.taxAmount)
                .subtract(this.discountAmount);
    }

    public void calculateSubtotal() {
    }

    // Helper methods
    public boolean canBeCancelled() {
        return this.status == OrderStatus.PENDING || this.status == OrderStatus.CONFIRMED;
    }

    public boolean isPaymentRequired(){
        return this.paymentMethod != PaymentMethod.COD;
    }

    public int getTotalItems() {
        return 0;
    }
}
