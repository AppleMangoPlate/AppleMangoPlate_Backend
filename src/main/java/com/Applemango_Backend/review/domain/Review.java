package com.Applemango_Backend.review.domain;

import com.Applemango_Backend.auth.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    // 멤버와 관계 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 가게 고유번호(카카오 api에서 제공하는 번호)
    private String storeId;
    private int rating; // 평점
    private String content; // 리뷰내용
    @CreatedDate
    private LocalDateTime createDate; // 생성일
    @LastModifiedDate
    private LocalDateTime modifiedDate; // 수정일

}
