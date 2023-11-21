package com.Applemango_Backend.bookmark.service;

import com.Applemango_Backend.bookmark.domain.Bookmark;
import com.Applemango_Backend.bookmark.domain.BookmarkRestaurant;
import com.Applemango_Backend.bookmark.dto.AddBookmarkRestaurantDto;
import com.Applemango_Backend.bookmark.repository.BookmarkRepository;
import com.Applemango_Backend.bookmark.repository.BookmarkRestaurantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final BookmarkRestaurantRepository bookmarkRestaurantRepository;

    //북마크 조회 기능은 UserService에 존재

    //북마크에 식당 추가
    @Transactional
    public Long addBookmarkRestaurant(AddBookmarkRestaurantDto addBookmarkRestaurantDto) {
        Bookmark bookmark = bookmarkRepository.findById(addBookmarkRestaurantDto.getBookmarkId()).orElseThrow(() ->
                new RuntimeException("Bookmark not found"));
        BookmarkRestaurant bookmarkRestaurant = BookmarkRestaurant.createBookmarkRestaurant(bookmark, addBookmarkRestaurantDto.getRestaurantName(), addBookmarkRestaurantDto.getRestaurantImage());

        return bookmarkRestaurantRepository.save(bookmarkRestaurant).getId();
    }

    //북마크에 즐겨찾기 식당 삭제
    @Transactional
    public String deleteBookmarkRestaurant(Long bookmarkRestaurantId) {
        BookmarkRestaurant bookmarkRestaurant = bookmarkRestaurantRepository.findById(bookmarkRestaurantId).orElseThrow(() ->
                new RuntimeException("Bookmark_restaurant not found"));
        bookmarkRestaurantRepository.delete(bookmarkRestaurant);

        return "Success deleteRestaurant";
    }

}
