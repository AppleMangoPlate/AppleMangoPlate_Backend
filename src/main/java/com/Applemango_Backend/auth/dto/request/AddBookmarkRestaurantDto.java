package com.Applemango_Backend.auth.dto.request;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddBookmarkRestaurantDto { // 북마크에 식당 추가 요청
    private Long BookmarkId;
    private String restaurantName;
    private String restaurantImage; //DB에 저장되어있는 리뷰 이미지 주소 or 식당 대표 이미지 주소 가져오기
}
