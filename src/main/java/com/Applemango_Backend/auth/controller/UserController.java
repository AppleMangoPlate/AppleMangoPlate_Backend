package com.Applemango_Backend.auth.controller;

import com.Applemango_Backend.auth.dto.GlobalResDto;
import com.Applemango_Backend.auth.dto.UpdateUserDto;
import com.Applemango_Backend.auth.service.UserService;
import com.Applemango_Backend.image.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/mypage")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class UserController {

    private final UserService userService;
    private final ImageUploadService imageUploadService;

    @PutMapping("/{email}/update")
    public GlobalResDto updateUser(@PathVariable String email, UpdateUserDto userDto) throws IOException {
        String imageUrl = "";
        if (userDto.getProfileImage() != null) {
            imageUrl = imageUploadService.uploadImage(userDto.getProfileImage());
        }

        return userService.updateUser(email,userDto,imageUrl);
    }
}
