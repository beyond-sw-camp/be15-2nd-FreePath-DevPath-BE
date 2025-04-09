package com.freepath.devpath.common.auth.service;

import com.freepath.devpath.common.exception.ErrorCode;
import com.freepath.devpath.user.exception.UserException;
import com.freepath.devpath.common.auth.dto.LoginRequest;
import com.freepath.devpath.common.auth.dto.TokenResponse;
import com.freepath.devpath.common.auth.domain.RefreshToken;
import com.freepath.devpath.common.jwt.JwtTokenProvider;
import com.freepath.devpath.user.command.entity.User;
import com.freepath.devpath.user.command.repository.UserCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserCommandRepository userCommandRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, RefreshToken> redisTemplate;

    public TokenResponse login(LoginRequest request) {
        User user = userCommandRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new BadCredentialsException("올바르지 않은 아이디 혹은 비밀번호"));

        // 유저 탈퇴 여부 확인
        validateUserStatus(user);

        // 요청에 담긴 password를 encoding한 값이 DB에 저장된 값과 동일한지 확인
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("올바르지 않은 아이디 혹은 비밀번호");
        }

        // 로그인 성공 시 token 발급
        String accessToken = jwtTokenProvider.createToken(String.valueOf(user.getUserId()), user.getUserRole().name());
        String refreshToken = jwtTokenProvider.createRefreshToken(String.valueOf(user.getUserId()), user.getUserRole().name());
        System.out.println(user.getUserId());


        // Redis에 value로 저장할 객체 생성
        RefreshToken redisRefreshToken = RefreshToken.builder()
                .token(refreshToken)
                .build();

        // Redis에 key를 loginId, value를 refreshToken 객체, TTL을 7일로 설정
        redisTemplate.opsForValue().set(
                String.valueOf(user.getUserId()),
                redisRefreshToken,
                Duration.ofDays(7)
        );

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    public TokenResponse refreshToken(String providedRefreshToken) {
        // 리프레시 토큰 유효성 검사, 저장 되어 있는 userId 추출
        jwtTokenProvider.validateToken(providedRefreshToken);
        String userId = jwtTokenProvider.getUsernameFromJWT(providedRefreshToken);

        // Redis에 저장된 리프레시 토큰 조회
        RefreshToken storedRefreshToken = redisTemplate.opsForValue().get(userId);
        if (storedRefreshToken == null) {
            throw new BadCredentialsException("해당 유저로 조회되는 리프레시 토큰 없음");
        }

        // 넘어온 리프레시 토큰과 Redis의 토큰 비교
        if (!storedRefreshToken.getToken().equals(providedRefreshToken)) {
            throw new BadCredentialsException("리프레시 토큰 일치하지 않음");
        }

        // user의 isUserRestricted와 userDeletedAt을 조회해서 탈퇴된 유저인지 판단하는 로직으로 수정 필요
        User user = userCommandRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new BadCredentialsException("해당 리프레시 토큰을 위한 유저 없음"));

        // 유저 탈퇴 여부 확인
        validateUserStatus(user);

        // 새로운 토큰 재발급
        String accessToken = jwtTokenProvider.createToken(String.valueOf(user.getUserId()), user.getUserRole().name());
        String refreshToken = jwtTokenProvider.createRefreshToken(String.valueOf(user.getUserId()), user.getUserRole().name());


        RefreshToken newToken = RefreshToken.builder()
                .token(refreshToken)
                .build();

        // Redis에 새로운 리프레시 토큰 저장 (기존 토큰 덮어쓰기)
        redisTemplate.opsForValue().set(
                String.valueOf(user.getUserId()),
                newToken,
                Duration.ofDays(7)
        );

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void logout(String refreshToken) {
        // refresh token의 서명 및 만료 검증
        jwtTokenProvider.validateToken(refreshToken);
        String userId = jwtTokenProvider.getUsernameFromJWT(refreshToken);
        redisTemplate.delete(userId);    // Redis에 저장된 refresh token 삭제
      }


    public void deleteUser(String userId, String password) {
        User user = userCommandRepository.findByUserIdAndUserDeletedAtIsNull(Integer.valueOf(userId))
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserException(ErrorCode.PASSWORD_NOT_MATCHED);
        }

        // 소프트 딜리트 처리
        user.markAsDeleted();
        userCommandRepository.save(user);

        // Redis에서 해당 loginId에 해당하는 리프레시 토큰 삭제
        Boolean existed = redisTemplate.hasKey(userId);
        if (Boolean.TRUE.equals(existed)) {
            redisTemplate.delete(userId);
        }
    }

    private void validateUserStatus(User user) {
        if (user.getUserDeletedAt() != null) {
            throw new DisabledException("탈퇴한 유저입니다.");
        }

        if ("Y".equals(user.getIsUserRestricted())) {
            throw new DisabledException("정지당한 유저입니다.");
        }
    }

    public void validateUserStatusByEmail(String email) {
        User user = userCommandRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        validateUserStatus(user);
    }
}
