package com.Applemango_Backend.auth.oauth.controller;

import com.Applemango_Backend.auth.oauth.dto.KakaoLoginResponse;
import com.Applemango_Backend.auth.oauth.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoService kakaoService;
    private final Logger logger = LoggerFactory.getLogger(KakaoController.class);

    @PostMapping("/login/oauth2/callback/kakao")
    public ResponseEntity<KakaoLoginResponse> kakaoLogin(@RequestParam("code") String code) {
        String kakaoAccessToken = kakaoService.getKakaoAccessToken(code).getAccess_token();
        return kakaoService.kakaoLogin(kakaoAccessToken);
    }


}
