package com.freepath.devpath.chattingroom.query.mapper;

import com.freepath.devpath.chattingroom.query.dto.ChattingRoomDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChattingRoomMapper {
    List<ChattingRoomDTO> selectChattingRooms(int userId);

}
