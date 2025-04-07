package com.freepath.devpath.user.command.service;

import com.freepath.devpath.user.exception.UserException;
import com.freepath.devpath.user.exception.UserErrorCode;
import com.freepath.devpath.user.command.dto.UserModifyRequest;
import com.freepath.devpath.user.command.entity.User;
import com.freepath.devpath.user.command.repository.UserRepository;
import com.freepath.devpath.user.command.dto.UserCreateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    public void modifyUser(UserModifyRequest request, String loginId) {
        Optional<User> userOptional = userRepository.findByLoginId(loginId);

        User user = userOptional.orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new UserException(UserErrorCode.PASSWORD_NOT_MATCHED);
        }

        String encodedPassword = passwordEncoder.encode(request.getNewPassword());
        user.update(request, encodedPassword);
        userRepository.save(user);
    }
}
