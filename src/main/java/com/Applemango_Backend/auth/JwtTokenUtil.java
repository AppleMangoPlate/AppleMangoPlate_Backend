package com.Applemango_Backend.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtTokenUtil {
    public static final String ACCESS_TOKEN = "Access_Token";
    public static final String REFRESH_TOKEN = "Refresh_Token";

    public static String createToken(String email, String key, long expireTimeMs) {

        //claim = jwt token에 들어갈 정보로, email을 넣어준다.
        Claims claims = Jwts.claims();
        claims.put("email", email);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    //claims에서 email 꺼내기
    public static String getEmail(String token, String secretKey) {
        return extractClaims(token, secretKey).get("email").toString();
    }

    //발급된 token의 만료시간이 지났는지 확인
    public static boolean isExpired(String token, String secretKey) {
        Date expiredDate = extractClaims(token, secretKey).getExpiration();

        return expiredDate.before(new Date());
    }

    //secretKey를 사용해 token 파싱
    private static Claims extractClaims(String token, String secretKey) {

        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }
}