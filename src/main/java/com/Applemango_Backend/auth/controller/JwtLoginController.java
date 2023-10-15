package com.Applemango_Backend.auth.controller;

import com.Applemango_Backend.auth.JwtTokenUtil;
import com.Applemango_Backend.auth.PrincipalDetails;
import com.Applemango_Backend.auth.dto.GlobalResDto;
import com.Applemango_Backend.auth.dto.JoinRequest;
import com.Applemango_Backend.auth.dto.LoginRequest;
import com.Applemango_Backend.auth.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/issue/token")
    public GlobalResDto issuedToken(@AuthenticationPrincipal PrincipalDetails userDetails, HttpServletResponse response) {
        response.addHeader(JwtTokenUtil.ACCESS_TOKEN, jwtTokenUtil.createToken(userDetails.getUsername(), "Access"));
        return new GlobalResDto("Success IssuedToken", HttpStatus.OK.value());
    }

}
