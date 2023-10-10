package auth.dto;

import auth.domain.User;
import auth.domain.UserRole;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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

    // 비밀번호 암호화
    public User toEntity(String encodedPassword) {
        return User.builder()
                .email(this.email)
                .password(encodedPassword)
                .nickName(this.nickName)
                .role(UserRole.USER)
                .phoneNumber(this.phoneNumber)
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .build();
    }
}