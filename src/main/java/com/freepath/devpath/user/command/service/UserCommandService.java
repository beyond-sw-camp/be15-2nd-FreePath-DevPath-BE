package com.freepath.devpath.user.command.service;

import com.freepath.devpath.email.config.RedisUtil;
import com.freepath.devpath.user.command.entity.User;
import com.freepath.devpath.user.command.repository.UserRepository;
import com.freepath.devpath.user.command.dto.UserCreateRequest;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCommandService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RedisUtil redisUtil;

//    @Transactional
//    public void registerUser(UserCreateRequest request) {
//        // 중복 회원 체크 로직 등 추가 기능
//        User user = modelMapper.map(request, User.class);
//        user.setEncodedPassword(passwordEncoder.encode(request.getPassword()));
//        userRepository.save(user);
//    }

    // UserCommandService.java
    public void saveTempUser(UserCreateRequest request) {
        String userJson = new Gson().toJson(request);
        redisUtil.setDataExpire("TEMP_USER:" + request.getEmail(), userJson, 60 * 30L); // 30분
    }

    // UserCommandService.java
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
        userRepository.save(user);

        // 인증 정보 삭제
        redisUtil.deleteData("TEMP_USER:" + email);
        redisUtil.deleteData("VERIFIED_USER:" + email);
    }

}
