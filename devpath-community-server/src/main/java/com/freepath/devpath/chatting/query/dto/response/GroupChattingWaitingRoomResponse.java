package com.freepath.devpath.chatting.query.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GroupChattingWaitingRoomResponse {
    private List<ChattingRoomWaitingDTO> chattingRoomWatingDTOList;
}
