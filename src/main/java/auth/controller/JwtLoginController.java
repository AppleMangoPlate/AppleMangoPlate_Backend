package auth.controller;

import auth.JwtTokenUtil;
import auth.domain.User;
import auth.dto.JoinRequest;
import auth.dto.LoginRequest;
import auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jwt-login")
public class JwtLoginController {

    private final UserService userService;

    @Value("${jwtmodule.secret-key}")
    private String secretKey;

    @PostMapping("/join")
    public String join(@RequestBody JoinRequest joinRequest) {

        // loginId 중복 체크
        if(userService.checkEmailDuplicate(joinRequest.getEmail())) {
            return "로그인 아이디가 중복됩니다.";
        }
        // 닉네임 중복 체크
        if(userService.checkNickNameDuplicate(joinRequest.getNickName())) {
            return "닉네임이 중복됩니다.";
        }
        // password와 passwordCheck가 같은지 체크
        if(!joinRequest.getPassword().equals(joinRequest.getPasswordCheck())) {
            return"바밀번호가 일치하지 않습니다.";
        }

        userService.join(joinRequest);
        return "회원가입 성공";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        User user= userService.login(loginRequest);

        if(user == null) {
            return "로그인 아이디 또는 비밀번호가 틀렸습니다.";
        }

        //로그인 성공 시, jwt token 발급
        long expireTimeMs = 1000 * 60 * 60; //token 유효시간 = 60분

        String jwtToken = JwtTokenUtil.createToken(user.getEmail(), secretKey, expireTimeMs);

        return jwtToken;
    }

    @GetMapping("/info")
    public String userInfo(Authentication authentication) {
        User loginUser = userService.getLoginUserByEmail(authentication.getName());

        return String.format("email : %s\nnickName : %s\nrole : %s",
                loginUser.getEmail(), loginUser.getNickName(), loginUser.getRole().name());
    }

    @GetMapping("/admin")
    public String adminPage() {

        return "관리자 페이지 접근 성공";
    }

}
