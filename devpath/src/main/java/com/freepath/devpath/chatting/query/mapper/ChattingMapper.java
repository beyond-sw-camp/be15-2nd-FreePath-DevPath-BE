package com.freepath.devpath.chatting.query.mapper;

import com.freepath.devpath.chatting.query.dto.response.ChattingDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChattingMapper {
    List<ChattingDTO> selectChattings(int chattingRoomId);
}
