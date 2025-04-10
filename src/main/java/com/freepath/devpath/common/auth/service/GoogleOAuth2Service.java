package com.freepath.devpath.common.auth.service;

import com.freepath.devpath.user.command.entity.User;
import com.freepath.devpath.user.command.entity.UserRole;
import com.freepath.devpath.user.command.repository.UserCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class GoogleOAuth2Service extends DefaultOAuth2UserService {

    private final UserCommandRepository userRepository;
    private final RedisTemplate<String, User> redisTemplate;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String email = oAuth2User.getAttribute("email");

        // 기존 회원 여부 확인
        User user = userRepository.findByEmailAndUserDeletedAtIsNull(email).orElse(null);

        if (user != null) {
            validateUserStatus(user);
        } else {
            // 신규 회원이면 Redis에 임시 저장
            String emailName = email.substring(0, email.indexOf('@'));
            String userName = oAuth2User.getAttribute("name");

            User tempUser = User.builder()
                    .email(email)
                    .loginId(emailName)
                    .password("") // 소셜 로그인은 비밀번호 없음
                    .loginMethod("GOOGLE")
                    .userRole(UserRole.USER)
                    .userName(userName)
                    .build();

            redisTemplate.opsForValue().set(
                    "SOCIAL_REGISTER:" + email,
                    tempUser,
                    Duration.ofMinutes(30)
            );
        }

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(
                        user != null ? user.getUserRole().name() : UserRole.USER.name())),
                oAuth2User.getAttributes(),
                "email"
        );
    }

    private void validateUserStatus(User user) {
        if (user.getUserDeletedAt() != null) {
            throw new DisabledException("탈퇴한 유저입니다.");
        }
        if ("Y".equals(user.getIsUserRestricted())) {
            throw new DisabledException("정지당한 유저입니다.");
        }
    }
}
