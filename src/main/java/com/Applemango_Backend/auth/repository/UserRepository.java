package com.Applemango_Backend.auth.repository;

import com.Applemango_Backend.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByNickName(String nickName);
    Optional<User> findByEmail(String email);
}