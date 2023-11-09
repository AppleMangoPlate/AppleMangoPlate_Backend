package com.Applemango_Backend.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class UpdateUserDto {
    private String nickName; //유저 별명
    private String phoneNumber; //유저 핸드폰번호
    private MultipartFile profileImage; //유저 프로필 사진
}
