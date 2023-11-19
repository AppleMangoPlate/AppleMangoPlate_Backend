package com.Applemango_Backend.review.repository;

import com.Applemango_Backend.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
