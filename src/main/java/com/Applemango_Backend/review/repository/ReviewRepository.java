package com.Applemango_Backend.review.repository;

import com.Applemango_Backend.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
     List<Review> findAllByStoreIdOrderByIdDesc(String storeId);
     List<Review> findAllByUserIdOrderByIdDesc(Long userId);

     @Query("SELECT AVG(r.rating) FROM Review r WHERE r.storeId = :storeId")
     Double getAverageRatingByStoreId(String storeId);

}
