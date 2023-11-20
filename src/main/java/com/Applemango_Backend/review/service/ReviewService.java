package com.Applemango_Backend.review.service;

import com.Applemango_Backend.auth.domain.User;
import com.Applemango_Backend.auth.dto.response.GlobalResDto;
import com.Applemango_Backend.auth.repository.UserRepository;
import com.Applemango_Backend.review.domain.Review;
import com.Applemango_Backend.review.domain.ReviewImage;
import com.Applemango_Backend.review.dto.GetReviewRes;
import com.Applemango_Backend.review.dto.PatchReviewReq;
import com.Applemango_Backend.review.dto.PostReviewReq;
import com.Applemango_Backend.review.repository.ReviewImageRepository;
import com.Applemango_Backend.review.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ReviewImageRepository reviewImageRepository;

    private final Logger logger = LoggerFactory.getLogger(ReviewService.class);


    public GlobalResDto postReview(PostReviewReq request, List<String> reviewImages) {

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


    public void deleteFile(Long reviewId) {
       reviewImageRepository.deleteReviewImageByReview_Id(reviewId);

    }


    public GlobalResDto modifyReview(PatchReviewReq request, List<String> reviewImages) {
        Review review=reviewRepository.findById(request.getReviewId()).orElse(null);
        if(review==null){
            logger.error("Review not found with ID: "+request.getReviewId());
            // 예외처리
        }
        review.updateReview(request.getRating(), request.getContent());
        for (String imageUrl : reviewImages) {
            ReviewImage reviewImage = ReviewImage.builder()
                    .review(review)
                    .reviewImage(imageUrl)
                    .build();
            reviewImageRepository.save(reviewImage);
        }

        return new GlobalResDto("Success patchReview", HttpStatus.OK.value());
    }



    public GlobalResDto deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if (review == null) {
            logger.error("Review not found with ID: " + reviewId);
            // 예외처리
        }
        reviewRepository.deleteById(review.getId());
        // 업로드된 사진 삭제
        deleteFile(reviewId);
        return new GlobalResDto("Success deleteReview", HttpStatus.OK.value());
    }

    public static String convertLocalDateTimeToLocalDate(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }


    public List<GetReviewRes> getReviews(String storeId) {
           List<Review> reviews = reviewRepository.findAllByStoreIdOrderByIdDesc(storeId);
           List<GetReviewRes> getReviewRes = reviews.stream()
                   .map(review -> {
                       List<String> reviewImages = reviewImageRepository.findReviewImagesByReview_Id(review.getId())
                               .stream()
                               .map(ReviewImage::getReviewImage)
                               .collect(Collectors.toList());
                       return new GetReviewRes(
                                review.getId(), review.getRating(), review.getUser().getNickName(),
                                review.getContent(), convertLocalDateTimeToLocalDate(review.getModifiedDate()),
                                reviewImages);
                    })
                    .collect(Collectors.toList());
            return getReviewRes;
    }

}
