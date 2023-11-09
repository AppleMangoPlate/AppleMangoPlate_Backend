package com.Applemango_Backend.auth.service;

import com.Applemango_Backend.auth.domain.RefreshToken;
import com.Applemango_Backend.auth.domain.User;
import com.Applemango_Backend.auth.dto.*;
import com.Applemango_Backend.auth.jwt.JwtTokenUtil;
import com.Applemango_Backend.auth.jwt.PrincipalDetails;
import com.Applemango_Backend.auth.repository.RefreshTokenRepository;
import com.Applemango_Backend.auth.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenUtil jwtTokenUtil;

    // Spring Security를 사용한 로그인 구현 시 사용
    private final BCryptPasswordEncoder encoder;


    //회원가입 시, 아이디 중복여부 확인
    public boolean checkEmailDuplicate(String email) {
        boolean flag = userRepository.existsByEmail(email);
        if (flag == false) {
            flag = true;
        }
        else {
            flag = false;
        }
        return flag;
    }

    //회원가입 시, 닉네임 중복여부 확인
    public boolean checkNickNameDuplicate(String nickName) {
        return userRepository.existsByNickName(nickName);
    }

    //JoinRequest를 입력받아 User로 변환 후 저장
    //이 과정에서 비밀번호는 암호화되어 저장
    @Transactional
    public GlobalResDto join(JoinRequest request, String imageUrl) {

        // password와 passwordCheck가 같은지 체크
        if(!request.getPassword().equals(request.getPasswordCheck())) {
            throw new RuntimeException("Not matches password and passwordcheck");
        }

        User user = User.joinUser(request.getEmail(),encoder.encode(request.getPassword()),request.getNickName(),request.getPhoneNumber(),imageUrl);
        userRepository.save(user);
        return new GlobalResDto("Success join", HttpStatus.OK.value());
    }

    @Transactional
    public GlobalResDto login(LoginRequest request, HttpServletResponse response) {

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() ->
                new RuntimeException("Not found user"));

        if(!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Not matches Password");
        }

        else {
            TokenDto tokenDto = jwtTokenUtil.createAllToken(request.getEmail());

            Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUserEmail(request.getEmail());

            if(refreshToken.isPresent()) {
                refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
            }
            else {
                RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), request.getEmail());
                refreshTokenRepository.save(newToken);
            }

            setHeader(response, tokenDto);

            return new GlobalResDto("Success Login", HttpStatus.OK.value());
        }
    }

    //유저 정보 수정
    @Transactional
    public GlobalResDto updateUser(PrincipalDetails userDetails, UpdateUserDto userDto, String imageUrl) {
        User user = userDetails.getUser();
        user.updateUser(userDto.getNickName(), userDto.getPhoneNumber(), imageUrl);

        return new GlobalResDto("Update User Info", HttpStatus.OK.value());
    }

    @Transactional
    public GlobalResDto logout(String userEmail) {
        if (refreshTokenRepository.findByUserEmail(userEmail) == null)
            throw new RuntimeException("Not found login user");
        refreshTokenRepository.deleteRefreshTokenByUserEmail(userEmail);

        return new GlobalResDto("Success Logout", HttpStatus.OK.value());
    }

    private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtTokenUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtTokenUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }
}
