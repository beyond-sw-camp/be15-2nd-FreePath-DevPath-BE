package com.freepath.devpath.common.auth.service;

import com.freepath.devpath.common.auth.dto.LoginRequest;
import com.freepath.devpath.common.auth.dto.TokenResponse;
import com.freepath.devpath.common.auth.entity.RefreshToken;
import com.freepath.devpath.common.auth.repository.RefreshTokenRepository;
import com.freepath.devpath.common.jwt.JwtTokenProvider;
import com.freepath.devpath.user.command.entity.User;
import com.freepath.devpath.user.command.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new BadCredentialsException("올바르지 않은 아이디 혹은 비밀번호"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("올바르지 않은 아이디 혹은 비밀번호");
        }

        // 로그인 성공 시 token 발급
        String accessToken = jwtTokenProvider.createToken(user.getLoginId(), user.getUserRole().name());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getLoginId(), user.getUserRole().name());
        // refreshToken은 서버측에서 관리되어야 하는 데이터이고 성능 상 추천되는 환경은 Redis
        // RDBMS에 저장해서 관리하는 코드로 작성
        RefreshToken tokenEntity = RefreshToken.builder()
                .username(user.getLoginId())
                .token(refreshToken)
                .expiryDate(
                        new Date(System.currentTimeMillis() + jwtTokenProvider.getRefreshExpiration())
                )
                .build();

        refreshTokenRepository.save(tokenEntity);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public TokenResponse refreshToken(String providedRefreshToken) {
        // 리프레시 토큰 유효성 검사
        jwtTokenProvider.validateToken(providedRefreshToken);
        String username = jwtTokenProvider.getUsernameFromJWT(providedRefreshToken);

        // 저장 된 refresh token 조회
        RefreshToken storedRefreshToken = refreshTokenRepository.findById(username)
                .orElseThrow(() -> new BadCredentialsException("해당 유저로 조회되는 리프레시 토큰 없음"));

        // 넘어온 리프레시 토큰 값과의 일치 확인
        if (!storedRefreshToken.getToken().equals(providedRefreshToken)) {
            throw new BadCredentialsException("리프레시 토큰 일치하지 않음");
        }

        // DB에 저장된 만료일과 현재 시간 비교 (추가 검증)
        if (storedRefreshToken.getExpiryDate().before(new Date())) {
            throw new BadCredentialsException("리프레시 토큰 유효시간 만료");
        }

        User user = userRepository.findByLoginId(username)
                .orElseThrow(() -> new BadCredentialsException("해당 리프레시 토큰을 위한 유저 없음"));

        // 새로운 토큰 재발급
        String accessToken = jwtTokenProvider.createToken(user.getLoginId(), user.getUserRole().name());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getLoginId(), user.getUserRole().name());

        RefreshToken tokenEntity = RefreshToken.builder()
                .username(user.getLoginId())
                .token(refreshToken)
                .expiryDate(
                        new Date(System.currentTimeMillis() + jwtTokenProvider.getRefreshExpiration())
                )
                .build();

        refreshTokenRepository.save(tokenEntity);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void logout(String refreshToken) {
        // refresh token의 서명 및 만료 검증
        jwtTokenProvider.validateToken(refreshToken);
        String username = jwtTokenProvider.getUsernameFromJWT(refreshToken);
        refreshTokenRepository.deleteById(username);    // DB에 저장된 refresh token 삭제

    }
}
