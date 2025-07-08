package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUserIdOrderByCreatedAtDesc(String userId);

    Optional<Wishlist> findByUserIdAndProductId(String userId, Long productId);

    boolean existsByUserIdAndProductId(String userId, Long productId);

    void deleteByUserIdAndProductId(String userId, Long productId);

    long countByUserId(String userId);

    @Query("SELECT w FROM Wishlist w JOIN FETCH w.product WHERE w.user.id = :userId ORDER BY w.createdAt DESC")
    List<Wishlist> findByUserIdWithProductDetails(@Param("userId") String userId);
}
