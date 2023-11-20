package com.Applemango_Backend.review.repository;

import com.Applemango_Backend.review.domain.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
    void deleteReviewImageByReview_Id(Long reviewId);
    List<ReviewImage> findReviewImagesByReview_Id(Long reviewId);
}
