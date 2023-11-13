package com.Applemango_Backend.bookmark.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "Bookmark_restaurant")
@NoArgsConstructor
public class BookmarkRestaurant { //북마크에 저장된 식당 테이블

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_restaurant_id")
    private Long id;

    private String restaurantName; //식당 이름
    private String restaurantImage; //식당 대표 이미지

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookmark_id")
    private Bookmark bookmark; // 북마크

    //생성 메서드
    public static BookmarkRestaurant createBookmarkRestaurant(Bookmark bookmark,String restaurantName, String restaurantImage) {
        return new BookmarkRestaurant(bookmark,restaurantName,restaurantImage);
    }

    protected BookmarkRestaurant(Bookmark bookmark, String restaurantName, String restaurantImage) {
        this.bookmark = bookmark;
        this.restaurantName = restaurantName;
        this.restaurantImage = restaurantImage;
    }

}
