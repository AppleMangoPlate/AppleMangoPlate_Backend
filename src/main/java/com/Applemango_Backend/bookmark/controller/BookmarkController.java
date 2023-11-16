package com.Applemango_Backend.bookmark.controller;

import com.Applemango_Backend.bookmark.service.BookmarkService;
import com.Applemango_Backend.bookmark.dto.AddBookmarkRestaurantDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmark")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    //북마크에 식당 추가
    @PostMapping("/{bookmarkId}/restaurant")
    public Long addBookmarkRestaurant(@PathVariable Long bookmarkId, AddBookmarkRestaurantDto addBookmarkRestaurantDto) {
        addBookmarkRestaurantDto.setBookmarkId(bookmarkId);

        return bookmarkService.addBookmarkRestaurant(addBookmarkRestaurantDto);
    }

    //북마크에서 식당 삭제
    @DeleteMapping("/{bookmarkId}/restaurant/{bookmarkRestaurantId}")
    public void deleteBookmarkRestaurant(@PathVariable Long bookmarkId, @PathVariable Long bookmarkRestaurantId) {
        bookmarkService.deleteBookmarkRestaurant(bookmarkRestaurantId);
    }

}
