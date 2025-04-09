package com.freepath.devpath.user.query.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    String findLoginIdByEmail(String email);
}
