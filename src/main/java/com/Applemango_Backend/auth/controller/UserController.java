package com.Applemango_Backend.auth.controller;


import com.Applemango_Backend.auth.dto.GlobalResDto;
import com.Applemango_Backend.auth.dto.UpdateUserDto;
import com.Applemango_Backend.auth.jwt.PrincipalDetails;
import com.Applemango_Backend.auth.service.UserService;
import com.Applemango_Backend.image.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/mypage")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class UserController {

    private final UserService userService;
    private final ImageUploadService imageUploadService;

    @PutMapping("/update")
    public GlobalResDto updateUser(@AuthenticationPrincipal PrincipalDetails userDetails, UpdateUserDto userDto) throws IOException {
        String imageUrl = "";
        if (userDto.getProfileImage() != null) {
            imageUrl = imageUploadService.uploadImage(userDto.getProfileImage());
        }

        return userService.updateUser(userDetails,userDto,imageUrl);
    }
}
