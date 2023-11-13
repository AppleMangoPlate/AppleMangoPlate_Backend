package com.Applemango_Backend.auth.controller;

import com.Applemango_Backend.bookmark.dto.BookmarkDto;
import com.Applemango_Backend.auth.dto.response.GlobalResDto;
import com.Applemango_Backend.auth.dto.request.UpdateUserDto;
import com.Applemango_Backend.auth.dto.response.UserDto;
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

    //유저 정보 조회
    @GetMapping("/{email}")
    public UserDto getUser(@PathVariable String email) {
        UserDto userDto = userService.getUser(email);
        return userDto;

    }
    //유저 정보 수정
    @PutMapping("/{email}/update")
    public GlobalResDto updateUser(@PathVariable String email, UpdateUserDto userDto) throws IOException {
        String imageUrl = "";
        if (userDto.getProfileImage() != null) {
            imageUrl = imageUploadService.uploadImage(userDto.getProfileImage());
        }

        return userService.updateUser(email,userDto,imageUrl);
    }

    //유저 프로필 이미지 삭제
    @DeleteMapping("/{email}/update/deleteImage")
    public void deleteUserImage(@PathVariable String email) {
        imageUploadService.deleteImage(email);
        userService.deleteUserImage(email);
    }

    //유저 북마크 조회
    @GetMapping("/{email}/bookmark")
    public BookmarkDto getUserBookmark(@PathVariable String email) {
        BookmarkDto bookmarkDto = userService.getUserBookmark(email);
        return bookmarkDto;
    }
}
