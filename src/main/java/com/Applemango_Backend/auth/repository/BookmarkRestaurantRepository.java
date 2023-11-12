package com.Applemango_Backend.auth.repository;

import com.Applemango_Backend.auth.domain.BookmarkRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRestaurantRepository extends JpaRepository<BookmarkRestaurant, Long> {

}
