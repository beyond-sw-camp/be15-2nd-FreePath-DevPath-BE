package com.freepath.devpath.chatting.query.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChattingRoomWaitingDTO {
    private int userId;
    private String nickname;
}
