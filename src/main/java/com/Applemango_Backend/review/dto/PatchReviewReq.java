package com.Applemango_Backend.review.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PatchReviewReq {
    private Long reviewId;
    private int rating;
    private String content;

}