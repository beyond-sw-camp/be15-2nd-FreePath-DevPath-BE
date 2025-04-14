package com.freepath.devpath.user.query.service;

import com.freepath.devpath.common.exception.ErrorCode;
import com.freepath.devpath.common.config.RedisUtil;
import com.freepath.devpath.user.exception.UserException;
import com.freepath.devpath.user.query.dto.response.UserEmail;
import com.freepath.devpath.user.query.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserQueryService {
    private final RedisUtil redisUtil;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public String findLoginIdByEmail(String email) {
        // 인증 여부 확인
        String verified = redisUtil.getData("VERIFIED_LOGINID:" + email);
        if (!"true".equals(verified)) {
            throw new UserException(ErrorCode.UNAUTHORIZED_EMAIL);
        }

        // loginId 조회
        String loginId = userMapper.findLoginIdByEmail(email);
        if (loginId == null) {
            throw new UserException(ErrorCode.USER_NOT_FOUND);
        }

        redisUtil.deleteData("TEMP_LOGINID:" + email);
        redisUtil.deleteData("VERIFIED_LOGINID:" + email);

        return loginId;
    }

    @Transactional(readOnly = true)
    public List<UserEmail> getSubscribedUsers() {

        return userMapper.findSubscribedUsers();
    }
}
