package com.Applemango_Backend.review.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostReviewReq {
    private String storeId;
    private int rating;
    private String content;

}
