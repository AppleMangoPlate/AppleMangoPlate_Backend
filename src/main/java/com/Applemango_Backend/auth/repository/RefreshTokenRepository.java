package com.Applemango_Backend.auth.repository;

import com.Applemango_Backend.auth.domain.RefreshToken;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUserEmail (String email);
    void deleteRefreshTokenByUserEmail (@NotNull String userEmail);
}
