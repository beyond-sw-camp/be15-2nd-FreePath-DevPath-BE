package com.freepath.devpath.chattingroom.query.dto;

import com.freepath.devpath.chattingroom.command.domain.aggregate.ChattingRoom;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ChattingRoomResponse {
    private final List<ChattingRoomDTO> chattingRooms;
}
