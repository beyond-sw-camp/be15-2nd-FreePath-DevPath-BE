package com.freepath.devpath.chatting.query.mapper;

import com.freepath.devpath.chatting.query.dto.response.UserBlockDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserBlockMapper {
    List<UserBlockDTO> selectUserBlocks(int userId);
}
