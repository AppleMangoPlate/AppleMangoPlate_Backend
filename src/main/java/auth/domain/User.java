package auth.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@Table(name = "User")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id; //유저번호
    private String email; //유저 이메일(아이디)
    private String password; //비밀번호
    private String nickName; //닉네임
    private String phoneNumber; //휴대폰 번호
    private UserRole role;
    private LocalDate createdAt; //생성일
    private LocalDate updatedAt; //수정일
}
