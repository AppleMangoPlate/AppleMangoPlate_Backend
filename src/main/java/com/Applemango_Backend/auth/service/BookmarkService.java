package com.Applemango_Backend.auth.service;

import com.Applemango_Backend.auth.domain.Bookmark;
import com.Applemango_Backend.auth.domain.BookmarkRestaurant;
import com.Applemango_Backend.auth.dto.request.AddBookmarkRestaurantDto;
import com.Applemango_Backend.auth.dto.response.GlobalResDto;
import com.Applemango_Backend.auth.repository.BookmarkRepository;
import com.Applemango_Backend.auth.repository.BookmarkRestaurantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final BookmarkRestaurantRepository bookmarkRestaurantRepository;

    //북마크 조회 기능은 UserService에 존재

    //북마크에 식당 추가
    @Transactional
    public GlobalResDto addBookmarkRestaurant(AddBookmarkRestaurantDto addBookmarkRestaurantDto) {
        Bookmark bookmark = bookmarkRepository.findById(addBookmarkRestaurantDto.getBookmarkId()).orElseThrow(() ->
                new RuntimeException("Bookmark not found"));
        BookmarkRestaurant bookmarkRestaurant = BookmarkRestaurant.createBookmarkRestaurant(bookmark, addBookmarkRestaurantDto.getRestaurantName(), addBookmarkRestaurantDto.getRestaurantImage());
        bookmarkRestaurantRepository.save(bookmarkRestaurant);
        return new GlobalResDto("Add restaurant in bookmark", HttpStatus.OK.value());
    }

    //북마크에 즐겨찾기 식당 삭제
    @Transactional
    public void deleteBookmarkRestaurant(Long bookmarkRestaurantId) {
        BookmarkRestaurant bookmarkRestaurant = bookmarkRestaurantRepository.findById(bookmarkRestaurantId).orElseThrow(() ->
                new RuntimeException("Bookmark_restaurant not found"));
        bookmarkRestaurantRepository.delete(bookmarkRestaurant);
    }

}
