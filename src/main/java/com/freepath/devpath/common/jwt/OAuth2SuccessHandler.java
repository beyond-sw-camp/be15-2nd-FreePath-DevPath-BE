package com.freepath.devpath.common.jwt;

import com.freepath.devpath.common.auth.domain.RefreshToken;
import com.freepath.devpath.user.command.repository.UserCommandRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, RefreshToken> redisTemplate;
    private final UserCommandRepository userRepository;

    @Value("${app.oauth2.redirect-url}")
    private String redirectUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        String email = ((org.springframework.security.oauth2.core.user.DefaultOAuth2User) authentication.getPrincipal()).getAttribute("email");

        userRepository.findByLoginIdAndLoginMethodAndUserDeletedAtIsNull(email, "GOOGLE").ifPresentOrElse(user -> {
            // DB에 유저가 존재하는 경우 → 토큰 발급
            String accessToken = jwtTokenProvider.createToken(String.valueOf(user.getUserId()), user.getUserRole().name());
            String refreshToken = jwtTokenProvider.createRefreshToken(String.valueOf(user.getUserId()), user.getUserRole().name());

            RefreshToken refreshTokenObj = RefreshToken.builder()
                    .token(refreshToken)
                    .build();

            redisTemplate.opsForValue().set(
                    String.valueOf(user.getUserId()),
                    refreshTokenObj,
                    Duration.ofDays(7)
            );

            try {
                response.sendRedirect(redirectUrl + "/after.html" + "?accessToken=" + accessToken + "&refreshToken=" + refreshToken);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }, () -> {
            // DB에 유저가 없으면 → 소셜 회원가입 페이지로 리다이렉트
            try {
                response.sendRedirect(redirectUrl + "/signup-social.html?email=" + email);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}