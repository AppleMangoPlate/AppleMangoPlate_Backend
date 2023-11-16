package com.Applemango_Backend.bookmark.repository;

import com.Applemango_Backend.bookmark.domain.BookmarkRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRestaurantRepository extends JpaRepository<BookmarkRestaurant, Long> {

}
