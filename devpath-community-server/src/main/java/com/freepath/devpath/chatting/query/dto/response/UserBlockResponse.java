package com.freepath.devpath.chatting.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class UserBlockResponse {
    private List<UserBlockDTO> userBlockDTOList;
}
