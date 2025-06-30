package com.example.ecommerce.entity;

import com.example.ecommerce.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, length = 255)
    String name;

    @Column(unique = true, nullable = false, length = 255)
    String slug;

    @Column(columnDefinition = "TEXT")
    String description;

    @Column(name = "short_description", columnDefinition = "TEXT")
    String shortDescription;

    @Column(unique = true, nullable = false, length = 100)
    String sku;

    @Column(nullable = false, precision = 12, scale = 2)
    BigDecimal price;

    @Column(name = "compare_price", precision = 12, scale = 2)
    BigDecimal comparePrice;

    @Column(name = "cost_price", precision = 12, scale = 2)
    BigDecimal costPrice;

    @Column(name = "stock_quantity")
    Integer stockQuantity = 0;

    @Column(name = "low_stock_threshold")
    Integer lowStockThreshold = 5;

    @Column(precision = 8, scale = 2)
    BigDecimal weight;

    @Column(length = 100)
    String dimensions;

    @Column(length = 100)
    String brand;

    @Column(length = 100)
    String model;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    ProductStatus status = ProductStatus.ACTIVE;

    @Column(name = "is_featured")
    Boolean isFeatured = false;

    @Column(name = "requiresShipping")
    Boolean requiresShipping = true;

    @Column(name = "meta_title", length = 255)
    String metaTitle;

    @Column(name = "meta_description", columnDefinition = "TEXT")
    String metaDescription;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "product_categories",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    Set<Category> categories = new HashSet<>();

    // One-to-many with ProductImage
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    Set<ProductImage> images = new HashSet<>();

    // One-to-many with ProductVariant
    //    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //    private Set<ProductVariant> variants = new HashSet<>();
}
