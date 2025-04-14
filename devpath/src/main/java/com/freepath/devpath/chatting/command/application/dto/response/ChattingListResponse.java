package com.freepath.devpath.chatting.command.application.dto.response;

import com.freepath.devpath.chatting.query.dto.response.ChattingDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ChattingListResponse {
    List<ChattingDTO> chattingList;
}
