package com.example.ecommerce.repository;

import com.example.ecommerce.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    boolean existsBySku(String sku);

    List<ProductVariant> findByProductIdAndIsActiveTrueOrderByCreatedAt(Long productId);
}
