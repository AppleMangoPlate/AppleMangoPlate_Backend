package com.Applemango_Backend.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetReviewRes {
    private Long reviewId;
    private int rating; // 평점
    private String nickName;
    private String content;
    private String createdDate; // ex) 2023-07-03
    // private String createTime; // ex) 4분전
    List<String> reviewImages;
}
