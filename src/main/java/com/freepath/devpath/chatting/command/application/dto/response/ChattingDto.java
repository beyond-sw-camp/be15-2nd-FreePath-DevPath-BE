package com.freepath.devpath.chatting.command.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChattingDto {
    private String nickname;
    private String message;
    private String timestamp;
}
