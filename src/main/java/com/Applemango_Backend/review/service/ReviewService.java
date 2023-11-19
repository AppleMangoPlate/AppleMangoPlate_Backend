package com.Applemango_Backend.review.service;

import com.Applemango_Backend.auth.domain.User;
import com.Applemango_Backend.auth.dto.response.GlobalResDto;
import com.Applemango_Backend.auth.repository.UserRepository;
import com.Applemango_Backend.review.domain.Review;
import com.Applemango_Backend.review.domain.ReviewImage;
import com.Applemango_Backend.review.dto.ReviewRequest;
import com.Applemango_Backend.review.repository.ReviewImageRepository;
import com.Applemango_Backend.review.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ReviewImageRepository reviewImageRepository;

    private final Logger logger = LoggerFactory.getLogger(ReviewService.class);


    public GlobalResDto postReview(ReviewRequest request, List<String> reviewImages) {

        User user = userRepository.findById(request.getUserId()).orElse(null);
        if (user == null) {
            // 사용자가 없는 경우에 대한 예외 처리
            logger.error("User not found with ID: " + request.getUserId());
            // return;
        }
        Review review = Review.builder()
                .user(user)
                .storeId(request.getStoreId())
                .rating(request.getRating())
                .content(request.getContent())
                .build();
        reviewRepository.save(review);
        for (String imageUrl : reviewImages) {
            ReviewImage reviewImage = ReviewImage.builder()
                    .review(review)
                    .reviewImage(imageUrl)
                    .build();
            reviewImageRepository.save(reviewImage);
        }
        return new GlobalResDto("Success postReview", HttpStatus.OK.value());
    }
}
