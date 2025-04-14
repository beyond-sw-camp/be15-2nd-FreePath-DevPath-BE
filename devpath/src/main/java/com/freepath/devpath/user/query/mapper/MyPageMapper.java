package com.freepath.devpath.user.query.mapper;

import com.freepath.devpath.user.query.dto.response.UserInfoResponse;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MyPageMapper {
    UserInfoResponse findUserInfoByUserId(Integer userId);
}
