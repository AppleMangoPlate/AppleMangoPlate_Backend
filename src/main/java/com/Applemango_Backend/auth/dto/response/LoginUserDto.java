package com.Applemango_Backend.auth.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginUserDto { //로그인한 유저의 이메일과 토큰 response dto
    private String userEmail;
    private String accessToken;
    private String refreshToken;

    public LoginUserDto(String userEmail, String accessToken, String refreshToken) {
        this.userEmail = userEmail;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
