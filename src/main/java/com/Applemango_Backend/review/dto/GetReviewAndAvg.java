package com.Applemango_Backend.review.dto;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class GetReviewAndAvg {
    private List<GetReviewRes> review;
    private Double avgRating;

}
