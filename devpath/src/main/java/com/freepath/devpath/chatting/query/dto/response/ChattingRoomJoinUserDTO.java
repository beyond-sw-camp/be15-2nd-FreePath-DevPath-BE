package com.freepath.devpath.chatting.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChattingRoomJoinUserDTO {
    int userId;
    String nickname;
}
