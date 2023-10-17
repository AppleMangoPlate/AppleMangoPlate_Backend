package com.Applemango_Backend.auth.oauth.dto;

import com.Applemango_Backend.auth.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KakaoLoginResponse {
    private boolean loginSuccess;
    private long userId;
    private String email;
}
