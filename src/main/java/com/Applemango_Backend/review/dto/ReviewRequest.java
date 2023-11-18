package com.Applemango_Backend.review.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class ReviewRequest {
    private Long userId;
    private String storeId;
    private int rating;
    private String content;
    private MultipartFile reviewImage;

}
