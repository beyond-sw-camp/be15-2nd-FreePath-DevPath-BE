package com.freepath.devpath.chatting.query.mapper;

import com.freepath.devpath.chatting.command.domain.aggregate.ChattingRoom;
import com.freepath.devpath.chatting.query.dto.response.ChattingRoomDTO;
import com.freepath.devpath.chatting.query.dto.response.ChattingRoomWaitingDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ChattingRoomMapper {
    List<ChattingRoomDTO> selectChattingRooms(int userId);
    List<ChattingRoomWaitingDTO> selectWaitingUsers(int chattingRoomId);

}
