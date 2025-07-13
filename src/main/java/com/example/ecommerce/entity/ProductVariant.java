package com.example.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "product_variants")
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

    @Column(unique = true, nullable = false, length = 100)
    String sku;

    @Column(name = "name_suffix", length = 100)
    String nameSuffix; // 256GB - Natural Titanium

    @Column(precision = 12, scale = 2)
    BigDecimal price; // Giá riêng nếu khác với product base price

    @Column(name = "compare_price", precision = 12, scale = 2)
    BigDecimal comparePrice;

    @Column(name = "stock_quantity")
    Integer stockQuantity;

    //Json field chứa các thuộc tính variant
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "JSON")
    Map<String, Object> attributes;

    @Column(precision = 8, scale = 2)
    BigDecimal weight;

    @Column(length = 100)
    String dimensions;

    @Column(name = "is_active")
    Boolean isActive = true;

    @Column(name = "is_in_stock")
    Boolean isInStock;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    // Helper methods
    public String getDisplayName(){
        return product.getName() + (nameSuffix != null ? " - " + nameSuffix : "");
    }

    public BigDecimal getEffectivePrice() {
        return price != null ? price : product.getPrice();
    }

    public BigDecimal getEffectiveComparePrice() {
        return comparePrice != null ? comparePrice : product.getComparePrice();
    }

    public String getAttributeValue(String attributeName) {
        if (attributes == null) return null;
        Object value = attributes.get(attributeName);
        return value != null ? value.toString() : null;
    }
}
