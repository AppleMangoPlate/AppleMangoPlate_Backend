package com.Applemango_Backend.auth.oauth.service;

import com.Applemango_Backend.auth.domain.RefreshToken;
import com.Applemango_Backend.auth.domain.User;
import com.Applemango_Backend.auth.dto.GlobalResDto;
import com.Applemango_Backend.auth.dto.TokenDto;
import com.Applemango_Backend.auth.jwt.JwtTokenUtil;
import com.Applemango_Backend.auth.oauth.dto.KakaoLoginResponse;
import com.Applemango_Backend.auth.oauth.dto.KakaoTokenDto;
import com.Applemango_Backend.auth.repository.RefreshTokenRepository;
import com.Applemango_Backend.auth.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@Service
@RequiredArgsConstructor
public class KakaoService {

    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final Logger logger = LoggerFactory.getLogger(KakaoService.class);

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String client_id;
    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String client_secret;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirect_uri;


    public KakaoTokenDto getKakaoAccessToken(String code) {

        // 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // Http response body 객체 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", client_id); // client_id
        params.add("redirect_uri", redirect_uri); // redirect_uri
        params.add("code", code);
        params.add("client_secret", client_secret); // client_secret

        // 헤더, 바디 합치기 위해 http entity 객체 생성
        HttpEntity<MultiValueMap<String, String>> kakaoTokenReq = new HttpEntity<>(params, headers);

        // 카카오로부터 accesstoken 받아오기
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> accessTokenRes = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenReq,
                String.class
        );

        // parsing
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        KakaoTokenDto kakaoTokenDto = null;
        try {
            kakaoTokenDto = om.readValue(accessTokenRes.getBody(), KakaoTokenDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return kakaoTokenDto;

    }

    public KakaoLoginResponse kakaoLogin(String kakaoAccessToken, HttpServletResponse response) {
        User user = getKakaoInfo(kakaoAccessToken);

        // 회원가입 되어있는지 이메일로 확인
        // 회원가입 안되어있으면 save 시키고 jwt 발급
        // 회원가입 되어있으면 바로 jwt 발급
        User existOwner = userRepository.findByEmail(user.getEmail()).orElse(null);
            if (existOwner == null) { // if 회원가입 x
                logger.info("첫 로그인 회원");
                userRepository.save(user);
            }

        TokenDto tokenDto = jwtTokenUtil.createAllToken(user.getEmail());

        RefreshToken refreshToken = refreshTokenRepository.findByUserEmail(user.getEmail()).orElse(null);
        if(refreshToken!=null){
            refreshTokenRepository.save(refreshToken.updateToken(tokenDto.getRefreshToken()));
        } else{
            RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), user.getEmail());
            refreshTokenRepository.save(newToken);
        }


        response.addHeader(JwtTokenUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtTokenUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());


        return new KakaoLoginResponse(true, existOwner==null ? user.getId() : existOwner.getId(), user.getEmail());
    }


    public User getKakaoInfo(String kakaoAccessToken) {
        // HttpHeader 생성
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + kakaoAccessToken);
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpHeader와 HttpBody를 하나의 객체에 담기(body 정보는 생략 가능)
        HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);

        // RestTemplate를 이용하여 HTTP 요청 처리
        RestTemplate restTemplate = new RestTemplate();

        // Http 요청을 GET 방식으로 실행하여 멤버 정보를 가져옴
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                requestEntity,
                String.class
        );

        // 카카오 인증 서버가 반환한 사용자 정보
        String userInfo = responseEntity.getBody();

        // JSON 데이터에서 필요한 정보 추출
        Gson gsonObj = new Gson();
        Map<?, ?> data = gsonObj.fromJson(userInfo, Map.class);

        // 이메일 동의 여부 확인
        boolean emailAgreement = (boolean) ((Map<?, ?>) (data.get("kakao_account"))).get("email_needs_agreement");
        String email;
        if (emailAgreement) { // 사용자가 이메일 동의를 하지 않은 경우
            email = ""; // 대체값 설정
        } else { // 사용자가 이메일 제공에 동의한 경우
            // 이메일 정보 가져오기
            email = (String) ((Map<?, ?>) (data.get("kakao_account"))).get("email");
        }

        // 닉네임 동의 여부 확인
        boolean nickNameAgreement = (boolean) ((Map<?, ?>) (data.get("kakao_account"))).get("profile_nickname_needs_agreement");
        String nickName;
        if (nickNameAgreement) { // 사용자가 닉네임 동의를 하지 않은 경우
            nickName = ""; // 대체값 설정
        } else { // 사용자가 닉네임 제공에 동의한 경우
            // 닉네임 정보 가져오기
            nickName = (String) ((Map<?, ?>) ((Map<?, ?>) data.get("properties"))).get("nickname");
        }
        User user=new User().builder().
                email(email).
                nickName(nickName)
                .build();
        return user;
    }

}
