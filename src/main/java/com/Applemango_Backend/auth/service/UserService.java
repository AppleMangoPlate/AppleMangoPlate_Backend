package com.Applemango_Backend.auth.service;

import com.Applemango_Backend.auth.dto.response.LoginUserDto;
import com.Applemango_Backend.bookmark.domain.Bookmark;
import com.Applemango_Backend.auth.domain.RefreshToken;
import com.Applemango_Backend.auth.domain.User;
import com.Applemango_Backend.auth.dto.request.JoinRequest;
import com.Applemango_Backend.auth.dto.request.LoginRequest;
import com.Applemango_Backend.auth.dto.request.UpdateUserDto;
import com.Applemango_Backend.bookmark.dto.BookmarkDto;
import com.Applemango_Backend.auth.dto.response.TokenDto;
import com.Applemango_Backend.auth.dto.response.UserDto;
import com.Applemango_Backend.auth.jwt.JwtTokenUtil;
import com.Applemango_Backend.bookmark.repository.BookmarkRepository;
import com.Applemango_Backend.auth.repository.RefreshTokenRepository;
import com.Applemango_Backend.auth.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BookmarkRepository bookmarkRepository;
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
    public String join(JoinRequest request, String imageUrl) {

        // password와 passwordCheck가 같은지 체크
        if(!request.getPassword().equals(request.getPasswordCheck())) {
            throw new RuntimeException("Not matches password and passwordcheck");
        }

        User user = User.joinUser(request.getEmail(),encoder.encode(request.getPassword()),request.getNickName(),request.getPhoneNumber(),imageUrl);
        Bookmark bookmark = Bookmark.createBookmark(user); //회원가입 시 북마크 생성
        userRepository.save(user);
        bookmarkRepository.save(bookmark);
        return "Success Join";
    }

    @Transactional
    public LoginUserDto login(LoginRequest request, HttpServletResponse response) {

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

            String access_token = tokenDto.getAccessToken();
            String refresh_token = tokenDto.getRefreshToken();

            return new LoginUserDto(user.getEmail(),access_token,refresh_token);
        }
    }

    //유저 정보 수정
    @Transactional
    public String updateUser(String email, UpdateUserDto userDto, String imageUrl) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new RuntimeException("Not found user"));

        user.updateUser(userDto.getNickName(), userDto.getPhoneNumber(), imageUrl);

        return "Success updateUser";
    }

    //유저 정보 조회
    public UserDto getUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new RuntimeException("Not found user"));
        return new UserDto(user);
    }

    //유저 북마크 조회
    public BookmarkDto getUserBookmark(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new RuntimeException("Not found user"));
        return new BookmarkDto(user.getBookmark());
    }

    //유저 프로필이미지 삭제
    public String deleteUserImage(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new RuntimeException("Not found user"));
        String Null = "";
        user.setProfileImage(Null);

        return "Success delete profileImage";
    }

    @Transactional
    public String logout(String userEmail) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserEmail(userEmail).orElseThrow(() ->
                new RuntimeException("Not found login user"));
        refreshTokenRepository.delete(refreshToken);

        return "Success Logout";
    }

    private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtTokenUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtTokenUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }
}
