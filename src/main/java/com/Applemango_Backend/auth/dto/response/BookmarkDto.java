package com.Applemango_Backend.auth.dto.response;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BookmarkDto {
    private Long bookmarkId;
    private String email;
    private List<BookmarkRestaurantDto> bookmarkRestaurants = new ArrayList<>();
}
