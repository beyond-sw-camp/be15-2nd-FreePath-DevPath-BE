package com.freepath.devpath.chatting.command.application.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class ChattingRequest {
    private Integer chattingRoomId;
    private String chattingMessage;
}
