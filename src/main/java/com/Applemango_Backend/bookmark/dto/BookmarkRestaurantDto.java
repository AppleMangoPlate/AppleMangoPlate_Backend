package com.Applemango_Backend.bookmark.dto;

import com.Applemango_Backend.bookmark.domain.BookmarkRestaurant;
import lombok.Getter;

@Getter
public class BookmarkRestaurantDto {
    private Long bookmarkRestaurantId;
    private String restaurantName;
    private String restaurantImage;

    public BookmarkRestaurantDto(BookmarkRestaurant bookmarkRestaurant) {
        this.bookmarkRestaurantId = bookmarkRestaurant.getId();
        this.restaurantName = bookmarkRestaurant.getRestaurantName();
        this.restaurantImage = bookmarkRestaurant.getRestaurantImage();
    }
}
