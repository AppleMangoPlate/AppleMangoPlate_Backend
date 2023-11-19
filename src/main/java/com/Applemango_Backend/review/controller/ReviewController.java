package com.Applemango_Backend.review.controller;

import com.Applemango_Backend.auth.dto.response.GlobalResDto;
import com.Applemango_Backend.image.service.ImageUploadService;
import com.Applemango_Backend.review.dto.PatchReviewReq;
import com.Applemango_Backend.review.dto.PostReviewReq;
import com.Applemango_Backend.review.service.ReviewService;
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
    @PostMapping
    public GlobalResDto postReview(@RequestPart(value = "image", required = false) List<MultipartFile> reviewImages,
                                   @Validated @RequestPart(value = "postReviewReq") PostReviewReq request) throws IOException {
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
        return reviewService.postReview(request, imageUrls);
    }


    @PatchMapping
    public GlobalResDto modifyBoard(@RequestPart(value = "image", required = false) List<MultipartFile> reviewImages,
                                            @Validated @RequestPart(value = "patchReviewReq") PatchReviewReq patchBoardReq) {
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
        return reviewService.modifyReview(patchBoardReq, imageUrls);

    }



}
