package com.Applemango_Backend.auth.dto.response;


import com.Applemango_Backend.auth.domain.User;
import com.Applemango_Backend.auth.domain.UserRole;
import lombok.Getter;


@Getter
public class UserDto {
    private Long id;
    private String email;        //유저 이메일(아이디)
    private String nickName;
    private UserRole role;
    private String phoneNumber;
    private String profileImage; //유저 프로필 이미지

    public UserDto(User user) {
        this.id = user.getId();
        this.nickName = user.getNickName();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.phoneNumber = user.getPhoneNumber();
        this.profileImage = user.getProfileImage();
    }
}
