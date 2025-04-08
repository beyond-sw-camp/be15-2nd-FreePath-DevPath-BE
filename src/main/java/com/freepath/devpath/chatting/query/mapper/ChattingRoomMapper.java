package com.freepath.devpath.chatting.query.mapper;

import com.freepath.devpath.chatting.query.dto.ChattingRoomDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChattingRoomMapper {
    List<ChattingRoomDTO> selectChattingRooms(int userId);

}
