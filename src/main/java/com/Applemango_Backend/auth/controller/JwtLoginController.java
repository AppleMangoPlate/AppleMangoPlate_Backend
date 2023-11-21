package com.Applemango_Backend.auth.controller;

import com.Applemango_Backend.auth.dto.response.LoginUserDto;
import com.Applemango_Backend.auth.jwt.JwtTokenUtil;
import com.Applemango_Backend.auth.jwt.PrincipalDetails;
import com.Applemango_Backend.auth.dto.response.GlobalResDto;
import com.Applemango_Backend.auth.dto.request.JoinRequest;
import com.Applemango_Backend.auth.dto.request.LoginRequest;
import com.Applemango_Backend.auth.service.UserService;
import com.Applemango_Backend.exception.ApiResponse;
import com.Applemango_Backend.image.service.ImageUploadService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jwt-login")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class JwtLoginController {

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final ImageUploadService imageUploadService;

    @PostMapping("/join")
    public ApiResponse<String> join(JoinRequest joinRequest) throws IOException {
        String imageUrl = "";
        if (joinRequest.getProfileImage() != null) {
            imageUrl = imageUploadService.uploadImage(joinRequest.getProfileImage());
        }

        return new ApiResponse<>(userService.join(joinRequest, imageUrl));
    }

    @GetMapping(value = "/join/{email}")
    public ResponseEntity<Boolean> emailCheck(@PathVariable String email) {
       return ResponseEntity.ok(userService.checkEmailDuplicate(email));
    }

    @PostMapping("/login")
    public ApiResponse<LoginUserDto> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) {
        return new ApiResponse<>(userService.login(loginRequest, response));
    }

    @DeleteMapping("/logout/{userEmail}")
    public ApiResponse<String> logout(@PathVariable String userEmail) {
        return new ApiResponse<>(userService.logout(userEmail));
    }

    @GetMapping("/issue/token")
    public GlobalResDto issuedToken(@AuthenticationPrincipal PrincipalDetails userDetails, HttpServletResponse response) {
        response.addHeader(JwtTokenUtil.ACCESS_TOKEN, jwtTokenUtil.createToken(userDetails.getUser().getEmail(), "Access"));
        return new GlobalResDto("Success IssuedToken", HttpStatus.OK.value());
    }

}
