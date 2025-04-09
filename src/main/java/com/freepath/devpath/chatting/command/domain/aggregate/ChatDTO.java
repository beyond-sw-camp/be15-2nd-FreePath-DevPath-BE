package com.freepath.devpath.chatting.command.domain.aggregate;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class ChatDTO {
    private Integer chattingRoomId;
    private String chattingMessage;
}
