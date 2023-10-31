package com.Applemango_Backend.auth.dto;

import com.Applemango_Backend.auth.domain.User;
import com.Applemango_Backend.auth.domain.UserRole;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Data
@NoArgsConstructor
public class JoinRequest {
    @NotBlank(message = "로그인 아이디가 비어있습니다.")
    private String email;

    @NotBlank(message = "비밀번호가 비어있습니다.")
    private String password;
    private String passwordCheck;

    @NotBlank(message = "닉네임이 비어있습니다.")
    private String nickName;

    @NotBlank(message = "핸드폰 번호가 비어있습니다.")
    private String phoneNumber;

    private MultipartFile profileImage;

}