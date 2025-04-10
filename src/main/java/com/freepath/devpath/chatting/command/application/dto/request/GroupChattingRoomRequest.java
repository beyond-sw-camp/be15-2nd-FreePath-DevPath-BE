package com.freepath.devpath.chatting.command.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GroupChattingRoomRequest {
    private int chattingRoomId;
    private int inviteeId;
}
