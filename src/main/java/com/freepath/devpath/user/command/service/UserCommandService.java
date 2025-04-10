package com.freepath.devpath.user.command.service;

import com.freepath.devpath.user.exception.UserException;
import com.freepath.devpath.common.exception.ErrorCode;
import com.freepath.devpath.user.command.dto.UserModifyRequest;
import com.freepath.devpath.email.config.RedisUtil;
import com.freepath.devpath.user.command.entity.User;
import com.freepath.devpath.user.command.repository.UserCommandRepository;
import com.freepath.devpath.user.command.dto.UserCreateRequest;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserCommandService {
    private final UserCommandRepository userCommandRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RedisUtil redisUtil;
    @Qualifier("userRedisTemplate")
    private final RedisTemplate<String, User> userRedisTemplate;

    public void saveTempUser(UserCreateRequest request) {
        String userJson = new Gson().toJson(request);
        redisUtil.setDataExpire("TEMP_USER:" + request.getEmail(), userJson, 60 * 30L); // 30분
    }

    public void registerUserFromRedis(String email) {
        String verified = redisUtil.getData("VERIFIED_USER:" + email);
        if (!"true".equals(verified)) {
            throw new UserException(ErrorCode.UNAUTHORIZED_EMAIL);
        }

        String userJson = redisUtil.getData("TEMP_USER:" + email);
        if (userJson == null) {
            throw new IllegalStateException("임시 회원 정보가 존재하지 않습니다.");
        }

        UserCreateRequest request = new Gson().fromJson(userJson, UserCreateRequest.class);
        User user = modelMapper.map(request, User.class);
        user.setEncodedPassword(passwordEncoder.encode(request.getPassword()));
        userCommandRepository.save(user);

        // 인증 정보 삭제
        redisUtil.deleteData("TEMP_USER:" + email);
        redisUtil.deleteData("VERIFIED_USER:" + email);
    }

    @Transactional
    public void modifyUser(UserModifyRequest request, Integer userId) {
        User user = userCommandRepository.findByUserIdAndUserDeletedAtIsNull(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        String encodedPassword = user.getPassword();

        user.update(request, encodedPassword);
        userCommandRepository.save(user);
    }

    public boolean isEmailDuplicate(String email) {
        return userCommandRepository.existsByEmailAndUserDeletedAtIsNull(email);
    }

    public boolean isLoginIdDuplicate(String loginId) {
        return userCommandRepository.findByLoginId(loginId).isPresent();
    }

    public void updatePassword(String email, String loginId, String newPassword) {
        // 인증 여부 확인
        String verified = redisUtil.getData("VERIFIED_PASSWORD:" + email);
        if (!"true".equals(verified)) {
            throw new UserException(ErrorCode.UNAUTHORIZED_EMAIL);
        }

        User user = userCommandRepository.findByEmailAndLoginIdAndUserDeletedAtIsNull(email, loginId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        if (!"GENERAL".equalsIgnoreCase(user.getLoginMethod())) {
            throw new UserException(ErrorCode.SOCIAL_LOGIN_USER);
        }

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new UserException(ErrorCode.SAME_AS_OLD_PASSWORD);
        }

        String encodedPassword = passwordEncoder.encode(newPassword);
        int updatedCount = userCommandRepository.updatePassword(email, encodedPassword);
        if (updatedCount == 0) {
            throw new UserException(ErrorCode.USER_NOT_FOUND);
        }

        redisUtil.deleteData("TEMP_PASSWORD:" + email);
        redisUtil.deleteData("VERIFIED_PASSWORD:" + email);
    }

    public void updateEmail(String email, String newEmail) {
        String verified = redisUtil.getData("VERIFIED_EMAIL:" + email);
        if (!"true".equals(verified)) {
            throw new UserException(ErrorCode.UNAUTHORIZED_EMAIL);
        }

        userCommandRepository.findByEmailAndUserDeletedAtIsNull(email)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        int updatedCount = userCommandRepository.updateEmail(email, newEmail);
        if (updatedCount == 0) {
            throw new UserException(ErrorCode.USER_NOT_FOUND);
        }

        redisUtil.deleteData("TEMP_EMAIL:" + email);
        redisUtil.deleteData("VERIFIED_EMAIL:" + email);
    }

    public void completeSocialSignup(String email, String nickname, String itNewsSubscription) {
        // Redis에서 임시 유저 정보 조회
        User tempUser = userRedisTemplate.opsForValue().get("SOCIAL_REGISTER:" + email);
        if (tempUser == null) {
            throw new UserException(ErrorCode.SOCIAL_SIGNUP_EXPIRED);
        }

        // 닉네임 중복 확인
        if (userCommandRepository.existsByNicknameAndUserDeletedAtIsNull(nickname)) {
            throw new UserException(ErrorCode.NICKNAME_ALREADY_USED);
        }

        // 추가 정보 설정
        tempUser.setNickname(nickname);
        tempUser.setItNewsSubscription(itNewsSubscription);

        // DB에 저장
        userCommandRepository.save(tempUser);

        // Redis에서 제거
        userRedisTemplate.delete("SOCIAL_REGISTER:" + email);
    }

    public boolean isNicknameDuplicate(String nickname) {
        return userCommandRepository.existsByNicknameAndUserDeletedAtIsNull(nickname);
    }
}
