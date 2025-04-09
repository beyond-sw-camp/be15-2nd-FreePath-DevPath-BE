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

    public void saveTempUser(UserCreateRequest request) {
        String userJson = new Gson().toJson(request);
        redisUtil.setDataExpire("TEMP_USER:" + request.getEmail(), userJson, 60 * 30L); // 30분
    }

    public void registerUserFromRedis(String email) {
        String verified = redisUtil.getData("VERIFIED_USER:" + email);
        if (!"true".equals(verified)) {
            throw new IllegalStateException("이메일 인증이 완료되지 않았습니다.");
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

        // 비밀번호 변경 요청이 있을 경우만 검증 및 인코딩 수행
        if (request.getNewPassword() != null && !request.getNewPassword().isBlank()) {
            if (request.getCurrentPassword() == null ||
                    !passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                throw new UserException(ErrorCode.PASSWORD_NOT_MATCHED);
            }
            encodedPassword = passwordEncoder.encode(request.getNewPassword());
        }

        user.update(request, encodedPassword);
        userCommandRepository.save(user);
    }

    public boolean isEmailDuplicate(String email) {
        return userCommandRepository.existsByEmailAndUserDeletedAtIsNull(email);
    }

    public void updatePassword(String email, String loginId, String newPassword) {
        // 인증 여부 확인
        String verified = redisUtil.getData("VERIFIED_PASSWORD:" + email);
        if (!"true".equals(verified)) {
            throw new UserException(ErrorCode.UNAUTHORIZED_EMAIL);
        }

        User user = userCommandRepository.findByEmailAndLoginIdAndUserDeletedAtIsNull(email, loginId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new UserException(ErrorCode.SAME_AS_OLD_PASSWORD);
        }

        String encodedPassword = passwordEncoder.encode(newPassword);
        int updatedCount = userCommandRepository.updatePasswordByEmail(email, encodedPassword);
        if (updatedCount == 0) {
            throw new UserException(ErrorCode.USER_NOT_FOUND);
        }

        redisUtil.deleteData("TEMP_PASSWORD:" + email);
        redisUtil.deleteData("VERIFIED_PASSWORD:" + email);
    }
}
