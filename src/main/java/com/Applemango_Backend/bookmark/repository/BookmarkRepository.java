package com.Applemango_Backend.bookmark.repository;

import com.Applemango_Backend.bookmark.domain.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Optional<Bookmark> findByEmail(String email);
}
