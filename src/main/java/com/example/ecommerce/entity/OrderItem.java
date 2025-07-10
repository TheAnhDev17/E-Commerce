package com.example.ecommerce.entity;

import com.example.ecommerce.enums.OrderItemStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    // Order relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    Order order;

    // Product relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id")
    ProductVariant productVariant;  // Nullable nếu product không có variant

    // Product snapshot (Quan trọng - lưu lại thông tin tại thời điểm mua

    @Column(name = "product_name", nullable = false, length = 200)
    String productName;

    @Column(name = "product_sku", length = 100)
    String productSku;

    @Column(name = "variant_name", length = 100)
    String variantName; // Size M, Color Red, etc.

    @Column(name = "variant_sku", length = 100)
    String variantSku;

    @Column(name = "product_image_url", length = 500)
    String productImageUrl; // Main image

    // Pricing information
    @Column(name = "unit_price", precision = 10, scale = 2, nullable = false)
    BigDecimal unitPrice; // Giá tại thời điểm mua

    @Column(name = "original_price", precision = 10, scale = 2)
    BigDecimal originalPrice; // Giá gốc (trước khi giảm)

    @Column(name = "discount_amount", precision = 10, scale = 2, nullable = false)
    BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "quantity", nullable = false)
    Integer quantity;

    @Column(name = "subtotal", precision = 10, scale = 2, nullable = false)
    BigDecimal subtotal; // unitPrice * quantity - discountAmount

    // Item status (useful for partial fulfillment)
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    OrderItemStatus status = OrderItemStatus.PENDING;

    // Tracking per item (useful for multiple suppliers)
    @Column(name = "tracking_number")
    String trackingNumber;

    @Column(name = "shipped_at")
    LocalDateTime shippedAt;

    @Column(name = "delivered_at")
    LocalDateTime deliveredAt;

    // Review relationship
    @OneToOne(mappedBy = "orderItem", cascade = CascadeType.ALL)
    Review review;

    // Timestamps
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    // Additional fields
    @Column(name = "notes", length = 500)
    String notes; // Ghi chú cho item này

    @Column(name = "weight", precision = 8, scale = 2)
    BigDecimal weight; // Trọng lượng (để tính ship)

    @Column(name = "dimensions", length = 100)
    String dimensions; // "20x15x10 cm"

    @Column(name = "return_reason", length = 500)
    String returnReason;

    @Column(name = "return_date")
    LocalDateTime returnDate;

    // Calculated methods
    public void calculateSubtotal() {
        BigDecimal total = this.unitPrice.multiply(new BigDecimal(this.quantity));
        this.subtotal = total.subtract(this.discountAmount);
    }

    public BigDecimal getTotalDiscount() {
        if (this.originalPrice != null) {
            BigDecimal itemDiscount = this.originalPrice.subtract(this.unitPrice)
                    .multiply(new BigDecimal(this.quantity));
            return itemDiscount.add(this.discountAmount);
        }
        return this.discountAmount;
    }

    // Helper methods
    public String getFullProductName() {
        if (this.variantName != null && !this.variantName.trim().isEmpty()) {
            return this.productName + " - " + this.variantName;
        }
        return this.productName;
    }

    public String getDisplaySku() {
        return this.variantSku != null ? this.variantSku : this.productSku;
    }

    public boolean canBeReviewed() {
        return this.status == OrderItemStatus.DELIVERED && this.review == null;
    }

    public boolean canBeReturned() {
        return this.status == OrderItemStatus.DELIVERED &&
                this.deliveredAt != null &&
                this.deliveredAt.isAfter(LocalDateTime.now().minusDays(7)); // 7 days return policy
    }

}
