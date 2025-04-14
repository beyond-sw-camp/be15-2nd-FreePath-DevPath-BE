package com.freepath.devpath.chatting.query.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChattingRoomDTO {
    private int chattingRoomId;
    private String chattingRoomTitle;
    private int userCount;
}
