package com.freepath.devpath.user.query.service;

import com.freepath.devpath.common.exception.ErrorCode;
import com.freepath.devpath.user.exception.UserException;
import com.freepath.devpath.user.query.dto.response.UserInfoResponse;
import com.freepath.devpath.user.query.mapper.MyPageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final MyPageMapper myPageMapper;

    public UserInfoResponse getUserInfo(Integer userId) {
        UserInfoResponse userInfo = myPageMapper.findUserInfoByUserId(userId);

        if (userInfo == null) {
            throw new UserException(ErrorCode.USER_NOT_FOUND);
        }

        return userInfo;
    }
}
