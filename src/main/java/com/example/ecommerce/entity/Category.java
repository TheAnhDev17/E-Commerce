package com.example.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, length = 255)
    String name;

    @Column(unique = true, nullable = false, length = 255)
    String slug;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<Category> children = new HashSet<>();

    @Column(columnDefinition = "TEXT")
    String description;

    @Column(name = "image_url", length = 500)
    String imageUrl;

    @Column(name = "sort_order")
    Integer sortOrder = 0;

    @Column(name = "is_active")
    Boolean isActive;

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

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    Set<Product> products = new HashSet<>();
}
