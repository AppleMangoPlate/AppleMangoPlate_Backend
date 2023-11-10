package com.Applemango_Backend.auth.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "Bookmark")
@NoArgsConstructor
public class Bookmark { //User와 북마크(즐겨찾기 저장소)는 일대일 관계

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id")
    private Long id;

    @OneToOne(mappedBy = "Bookmark", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private User user; //북마크를 소유한 회원

    @OneToMany(mappedBy = "Bookmark", cascade = {CascadeType.REMOVE}, orphanRemoval = true) // orphanRemoval: 북마크에서 즐겨찾기 삭제하면 DB에서도 삭제
    private List<BookmarkRestaurant> bookmarkRestaurants = new ArrayList<>(); //북마크에 담긴 식당들

    //생성 메서드
    public static Bookmark createBookmark(User user) {
        Bookmark bookmark = new Bookmark(user);
        user.setBookmark(bookmark);
        return bookmark;
    }

    protected Bookmark(User user) { this.user = user; }

}
