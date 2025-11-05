package com.demo.repository;

import com.demo.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    List<Review> findByProductId(Long productId);
    
    List<Review> findByUserId(Long userId);
    
    List<Review> findByStatus(Review.ReviewStatus status);
    
    @Query("SELECT r FROM Review r WHERE r.product.id = :productId AND r.status = :status")
    List<Review> findByProductIdAndStatus(@Param("productId") Long productId,
                                           @Param("status") Review.ReviewStatus status);
    
    @Query("SELECT r FROM Review r WHERE r.product.id = :productId AND r.rating >= :minRating")
    List<Review> findHighRatedReviews(@Param("productId") Long productId,
                                       @Param("minRating") Integer minRating);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId AND r.status = 'APPROVED'")
    Double getAverageRatingForProduct(@Param("productId") Long productId);
}
