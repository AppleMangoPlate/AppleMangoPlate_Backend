package auth.service;

import auth.domain.User;
import auth.dto.JoinRequest;
import auth.dto.LoginRequest;
import auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    
    private final BCryptPasswordEncoder encoder;
    
    public boolean checkemailDuplicate(String email) { return userRepository.existsByEmail(email); }
    
    public boolean checkNickNameDuplicate(String nickName) { return userRepository.existsByNickName(nickName); }
    
    public void join(JoinRequest request) {
        userRepository.save(request.toEntity(encoder.encode(request.getPassword())));
    }
    
    public User login(LoginRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        
        //유저가 없다 -> 추후 예외처리
        if(optionalUser.isEmpty()) {
            return null;
        }
        
        User user = optionalUser.get();
        
        //비밀번호가 다르다 -> 추후 예외처리
        if(!user.getPassword().equals(request.getPassword())) {
            return null;
        }
        
        return user;
    }

    public User getLoginUserById (Long userId) {
        if(userId == null) return null;

        Optional<User> optionalUser = userRepository.findById(userId);

        if(optionalUser.isEmpty()) return null;

        return optionalUser.get();
    }

    public User getLoginUserByEmail(String email) {
        if(email == null) return null;

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isEmpty()) return null;

        return optionalUser.get();
    }
}
