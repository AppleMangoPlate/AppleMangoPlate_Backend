package com.Applemango_Backend.auth.controller;

import com.Applemango_Backend.auth.JwtTokenUtil;
import com.Applemango_Backend.auth.PrincipalDetails;
import com.Applemango_Backend.auth.domain.User;
import com.Applemango_Backend.auth.dto.GlobalResDto;
import com.Applemango_Backend.auth.dto.JoinRequest;
import com.Applemango_Backend.auth.dto.LoginRequest;
import com.Applemango_Backend.auth.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jwt-login")
public class JwtLoginController {

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/join")
    public GlobalResDto join(@RequestBody @Valid JoinRequest joinRequest) {
        return userService.join(joinRequest);
    }

    @PostMapping("/login")
    public GlobalResDto login(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) {
        return userService.login(loginRequest, response);
    }

    @GetMapping("/info")
    public String userInfo(Authentication authentication) {
        String email = authentication.getName();
        User loginUser = userService.getLoginUserByEmail(email);

        return String.format("email : %s\nnickName : %s\nphoneNumber : %s\nrole : %s",
                loginUser.getEmail(), loginUser.getNickName(), loginUser.getPhoneNumber(),loginUser.getRole().name());
    }

    @GetMapping("/admin")
    public String adminPage() {

        return "관리자 페이지 접근 성공";
    }

    @GetMapping("/issue/token")
    public GlobalResDto issuedToken(@AuthenticationPrincipal PrincipalDetails userDetails, HttpServletResponse response) {
        response.addHeader(JwtTokenUtil.ACCESS_TOKEN, jwtTokenUtil.createToken(userDetails.getUser().getEmail(), "Access"));
        return new GlobalResDto("Success IssuedToken", HttpStatus.OK.value());
    }

}
