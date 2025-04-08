package com.freepath.devpath.chattingroom.query.service;

import com.freepath.devpath.chattingroom.query.dto.ChattingRoomDTO;
import com.freepath.devpath.chattingroom.query.dto.ChattingRoomResponse;
import com.freepath.devpath.chattingroom.query.mapper.ChattingRoomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChattingRoomQueryService {
    private final ChattingRoomMapper chattingRoomMapper;

    public ChattingRoomResponse getChattingRooms(String username) {
        int userId = Integer.parseInt(username);
        List<ChattingRoomDTO> chattingRooms = chattingRoomMapper.selectChattingRooms(userId);
        return ChattingRoomResponse.builder()
                .chattingRooms(chattingRooms)
                .build();
    }
}
