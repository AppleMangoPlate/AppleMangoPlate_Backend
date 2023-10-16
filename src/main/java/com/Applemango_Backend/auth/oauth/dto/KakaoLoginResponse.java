package com.Applemango_Backend.auth.oauth.dto;

import com.Applemango_Backend.auth.domain.User;
import lombok.Data;

@Data
public class KakaoLoginResponse {
    public boolean loginSuccess;
    public User user;
}
