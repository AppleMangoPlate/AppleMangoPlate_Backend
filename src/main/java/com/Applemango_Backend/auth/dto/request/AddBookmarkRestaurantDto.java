package com.Applemango_Backend.auth.dto.request;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddBookmarkRestaurantDto { // 북마크에 식당 추가 요청
    private Long BookmarkId;
    private String restaurantName;
    private String restaurantImage;
}
