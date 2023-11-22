package com.Applemango_Backend.review.repository;

import com.Applemango_Backend.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
     List<Review> findAllByStoreIdOrderByIdDesc(String storeId);
     List<Review> findAllByUserIdOrderByIdDesc(Long userId);

}
