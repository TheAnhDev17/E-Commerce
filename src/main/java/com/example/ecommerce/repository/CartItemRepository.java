package com.example.ecommerce.repository;

import com.example.ecommerce.entity.CartItem;
import com.example.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserIdOrderByCreatedAtDesc(String userId);

    List<CartItem> findByUserOrderByCreatedAtDesc(User user);

    Optional<CartItem> findByUserIdAndProductIdAndProductVariantId(String userId, Long productId, Long variantId);

    void deleteByUserId(String userId);

    Long countByUserId(String userId);

    @Query("SELECT ci FROM CartItem ci JOIN FETCH ci.product LEFT JOIN FETCH ci.productVariant WHERE ci.user.id = :userId ORDER BY ci.createdAt DESC")
    List<CartItem> findByUserIdWithProductDetails(@Param("userId") String userId);
}
