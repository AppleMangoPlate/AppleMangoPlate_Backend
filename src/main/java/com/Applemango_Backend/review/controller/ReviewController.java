package com.Applemango_Backend.review.controller;

import com.Applemango_Backend.auth.dto.response.GlobalResDto;
import com.Applemango_Backend.auth.jwt.JwtTokenUtil;
import com.Applemango_Backend.exception.ApiException;
import com.Applemango_Backend.exception.ApiResponse;
import com.Applemango_Backend.image.service.ImageUploadService;
import com.Applemango_Backend.review.dto.GetReviewRes;
import com.Applemango_Backend.review.dto.PatchReviewReq;
import com.Applemango_Backend.review.dto.PostReviewReq;
import com.Applemango_Backend.review.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final ImageUploadService imageUploadService;
    private final Logger logger = LoggerFactory.getLogger(ReviewService.class);
    private final JwtTokenUtil jwtTokenUtil;
    @PostMapping
    public ApiResponse<String> postReview(@RequestPart(value = "image", required = false) List<MultipartFile> reviewImages,
                                          @Validated @RequestPart(value = "postReviewReq") PostReviewReq request, HttpServletRequest servletRequest) throws IOException {
        try {
            String token = jwtTokenUtil.getHeaderToken(servletRequest, "Access");
            String email= jwtTokenUtil.getEmailFromToken(token);
            List<String> imageUrls = new ArrayList<>();
            reviewImages.forEach(file -> {
                if (!file.isEmpty()) {
                    String imageUrl = null;
                    try {
                        imageUrl = imageUploadService.uploadImage(file);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    imageUrls.add(imageUrl);
                    logger.info("imageUrl: " + imageUrl);
                }
            });
            return new ApiResponse<>(reviewService.postReview(request, imageUrls, email));
        } catch (ApiException exception) {
            return new ApiResponse<>(exception.getStatus());
        }
    }


    @PatchMapping
    public ApiResponse<String> modifyBoard(@RequestPart(value = "image", required = false) List<MultipartFile> reviewImages,
                                           @Validated @RequestPart(value = "patchReviewReq") PatchReviewReq patchBoardReq, HttpServletRequest servletRequest) {
        try {
            String token = jwtTokenUtil.getHeaderToken(servletRequest, "Access");
            String email= jwtTokenUtil.getEmailFromToken(token);
            // 해당 reviewId에 해당하는 reviewImage 삭제
            reviewService.deleteFile(patchBoardReq.getReviewId());

            // request 사진파일들 추가
            List<String> imageUrls = new ArrayList<>();
            reviewImages.forEach(file -> {
                if (!file.isEmpty()) {
                    String imageUrl = null;
                    try {
                        imageUrl = imageUploadService.uploadImage(file);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    imageUrls.add(imageUrl);
                    logger.info("imageUrl: " + imageUrl);
                }
            });
            return new ApiResponse<>(reviewService.modifyReview(patchBoardReq, imageUrls, email));
        } catch (ApiException exception) {
            return new ApiResponse<>(exception.getStatus());
        }
    }


    @DeleteMapping("/{reviewId}")
    public ApiResponse<String> deleteReview(@PathVariable(name = "reviewId") Long reviewId, HttpServletRequest request){
        try{
            String token = jwtTokenUtil.getHeaderToken(request, "Access");
            String email= jwtTokenUtil.getEmailFromToken(token);
            return new ApiResponse<>(reviewService.deleteReview(reviewId, email));
        } catch (ApiException exception) {
            return new ApiResponse<>(exception.getStatus());
        }
    }

    @GetMapping("/{storeId}")
    public ApiResponse<List<GetReviewRes>> getReviews(@PathVariable(name = "storeId") String storeId, HttpServletRequest request) {
        try{
            String token = jwtTokenUtil.getHeaderToken(request, "Access");
            String email= jwtTokenUtil.getEmailFromToken(token);
            return new ApiResponse<>(reviewService.getReviews(storeId, email));
        } catch (ApiException exception) {
            return new ApiResponse<>(exception.getStatus());
        }
    }

    @GetMapping("/my")
    public ApiResponse<List<GetReviewRes>> getReviewsById(HttpServletRequest request) {
        try{
            String token = jwtTokenUtil.getHeaderToken(request, "Access");
            String email= jwtTokenUtil.getEmailFromToken(token);
            return new ApiResponse<>(reviewService.getReviewsById(email));
        } catch (ApiException exception) {
            return new ApiResponse<>(exception.getStatus());
        }
    }


}
