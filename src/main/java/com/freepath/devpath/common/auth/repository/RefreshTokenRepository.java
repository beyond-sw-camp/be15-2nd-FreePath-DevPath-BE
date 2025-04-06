package com.freepath.devpath.common.auth.repository;

import com.freepath.devpath.common.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    RefreshToken token(String token);
}
