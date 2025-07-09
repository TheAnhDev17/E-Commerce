package com.example.ecommerce.entity;

import com.example.ecommerce.enums.OrderStatus;
import com.example.ecommerce.enums.PaymentMethod;
import com.example.ecommerce.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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

}
