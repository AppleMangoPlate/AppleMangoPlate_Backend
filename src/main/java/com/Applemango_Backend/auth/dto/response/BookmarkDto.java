package com.Applemango_Backend.auth.dto.response;

import com.Applemango_Backend.auth.domain.Bookmark;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BookmarkDto { //유저 북마크 조회 요청에 대한 응답
    private Long bookmarkId;
    private String email;
    private List<BookmarkRestaurantDto> bookmarkRestaurants = new ArrayList<>();

    public BookmarkDto(Bookmark bookmark) {
        this.bookmarkId = bookmark.getId();
        this.email = bookmark.getUser().getEmail();
        this.bookmarkRestaurants = bookmark.getBookmarkRestaurants().stream().map(BookmarkRestaurantDto::new).collect(Collectors.toList());
    }
}
