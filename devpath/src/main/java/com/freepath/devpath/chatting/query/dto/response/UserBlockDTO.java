package com.freepath.devpath.chatting.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserBlockDTO {
    private int userId;
    private String nickname;
}
