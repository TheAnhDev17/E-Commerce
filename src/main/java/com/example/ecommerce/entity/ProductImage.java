package com.example.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

    @Column(name = "image_url", nullable = false, length = 500)
    String imageUrl;

    @Column(name = "alt_text", length = 255)
    String altText;

    @Column(name = "sort_order")
    Integer sortOrder = 0;

    @Column(name = "is_primary")
    Boolean isPrimary = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;
}
