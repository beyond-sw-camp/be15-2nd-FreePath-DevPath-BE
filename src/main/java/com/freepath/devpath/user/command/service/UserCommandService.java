package com.freepath.devpath.user.command.service;

import com.freepath.devpath.user.exception.UserException;
import com.freepath.devpath.common.exception.ErrorCode;
import com.freepath.devpath.user.command.dto.UserModifyRequest;
import com.freepath.devpath.user.command.entity.User;
import com.freepath.devpath.user.command.repository.UserRepository;
import com.freepath.devpath.user.command.dto.UserCreateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserCommandService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerUser(UserCreateRequest request) {
        // 중복 회원 체크 로직 등 추가 기능
        User user = modelMapper.map(request, User.class);
        user.setEncodedPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void modifyUser(UserModifyRequest request, Integer userId) {
        User user = userRepository.findByUserIdAndUserDeletedAtIsNull(userId)
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
        userRepository.save(user);
    }
}
