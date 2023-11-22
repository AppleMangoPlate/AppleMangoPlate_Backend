package com.Applemango_Backend.review.service;

import com.Applemango_Backend.auth.domain.User;
import com.Applemango_Backend.auth.dto.response.GlobalResDto;
import com.Applemango_Backend.auth.repository.UserRepository;
import com.Applemango_Backend.exception.ApiException;
import com.Applemango_Backend.exception.ApiResponseStatus;
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

import static com.Applemango_Backend.exception.ApiResponseStatus.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ReviewImageRepository reviewImageRepository;

    private final Logger logger = LoggerFactory.getLogger(ReviewService.class);


    public String postReview(PostReviewReq request, List<String> reviewImages, String email) {

        User user=userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new ApiException(NONE_EXIST_USER);
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
        return "Success postReview";
    }


    public void deleteFile(Long reviewId) {
        reviewImageRepository.deleteReviewImageByReview_Id(reviewId);

    }


    public String modifyReview(PatchReviewReq request, List<String> reviewImages, String email) {
        Review review=reviewRepository.findById(request.getReviewId()).orElse(null);
        if(review==null){
            throw new ApiException(NONE_EXIST_REVIEW);
        }
        User user=userRepository.findByEmail(email).orElse(null);
        if(user==null){
            throw new ApiException(NONE_EXIST_USER);
        } else if(user.getId()!=review.getUser().getId()){
            throw new ApiException(INVALID_USER_JWT);
        }
        review.updateReview(request.getRating(), request.getContent());
        for (String imageUrl : reviewImages) {
            ReviewImage reviewImage = ReviewImage.builder()
                    .review(review)
                    .reviewImage(imageUrl)
                    .build();
            reviewImageRepository.save(reviewImage);
        }

        return "Success patchReview";
    }



    public String deleteReview(Long reviewId, String email) {
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if (review == null) {
            throw new ApiException(NONE_EXIST_REVIEW);
        }
        User user=userRepository.findByEmail(email).orElse(null);
        if(user==null){
            throw new ApiException(NONE_EXIST_USER);
        } else if(user.getId()!=review.getUser().getId()){
            throw new ApiException(INVALID_USER_JWT);
        }
        reviewRepository.deleteById(review.getId());
        // 업로드된 사진 삭제
        deleteFile(reviewId);
        return "Success deleteReview";
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


    public List<GetReviewRes> getReviewsById(String email) {
        User user=userRepository.findByEmail(email).orElse(null);
        if(user==null){
            throw new ApiException(NONE_EXIST_USER);
        }
        List<Review> reviews=reviewRepository.findAllByUserIdOrderByIdDesc(user.getId());
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
