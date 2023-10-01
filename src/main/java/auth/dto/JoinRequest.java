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
    private String passwordChk;

    @NotBlank(message = "닉네임이 비었습니다.")
    private String nickName;

    @NotBlank(message = "전화번호가 비었습니다.")
    private String phoneNumber;

    //비밀번호 암호화
    public User toEntity(String encodedPassword) {
        return User.builder()
                .email(this.email)
                .password(encodedPassword)
                .nickName(this.nickName)
                .phoneNumber(this.phoneNumber)
                .role(UserRole.USER)
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .build();
    }
}
