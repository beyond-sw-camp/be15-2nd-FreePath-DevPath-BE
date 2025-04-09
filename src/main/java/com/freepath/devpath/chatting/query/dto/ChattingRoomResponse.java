package com.freepath.devpath.chatting.query.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ChattingRoomResponse {
    private final List<ChattingRoomDTO> chattingRooms;
}
