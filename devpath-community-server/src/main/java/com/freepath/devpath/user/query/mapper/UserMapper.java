package com.freepath.devpath.user.query.mapper;

import com.freepath.devpath.user.query.dto.response.UserEmail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    String findLoginIdByEmail(String email);
    List<UserEmail> findSubscribedUsers(); // 구독자 이메일 리스트 조회
}
