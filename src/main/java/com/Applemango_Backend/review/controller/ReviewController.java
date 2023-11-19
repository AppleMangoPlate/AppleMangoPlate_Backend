package com.Applemango_Backend.review.controller;

import com.Applemango_Backend.auth.dto.response.GlobalResDto;
import com.Applemango_Backend.auth.dto.response.UserDto;
import com.Applemango_Backend.image.service.ImageUploadService;
import com.Applemango_Backend.review.dto.ReviewRequest;
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
                                   @Validated @RequestPart(value = "postReviewReq") ReviewRequest request) throws IOException {
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
}
