package com.Applemango_Backend.auth.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;        //유저 이메일(아이디)
    private String password;
    private String nickName;
    private UserRole role;
    private String phoneNumber;
    private LocalDate createdAt; //생성일
    private LocalDate updatedAt; //수정일
    private String profileImage; //유저 프로필 이미지
    
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "bookmark_id")
    private Bookmark bookmark; //유저와 북마크(즐겨찾기 저장소)는 일대일 관계

    //생성 메서드
    public static User joinUser(String email, String encodedPassword, String nickName, String phoneNumber, String profileImage) {
        User user = new User();
        user.email = email;
        user.password=encodedPassword;
        user.nickName=nickName;
        user.role = UserRole.USER;
        user.phoneNumber = phoneNumber;
        user.createdAt = LocalDate.now();
        user.updatedAt = LocalDate.now();
        user.profileImage=profileImage;

        return user;
    }

    //유저 정보 수정
    public void updateUser(String nickName, String phoneNumber, String profileImage) {
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
        this.profileImage = profileImage;
        this.updatedAt = LocalDate.now();
    }
    
    public void setBookmark(Bookmark bookmark) { this.bookmark = bookmark; }

    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }
}
