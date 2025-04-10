package com.freepath.devpath.chatting.query.service;

import com.freepath.devpath.chatting.command.domain.aggregate.ChattingJoin;
import com.freepath.devpath.chatting.command.domain.aggregate.ChattingJoinId;
import com.freepath.devpath.chatting.command.domain.aggregate.ChattingRole;
import com.freepath.devpath.chatting.command.domain.repository.ChattingJoinRepository;
import com.freepath.devpath.chatting.command.domain.repository.ChattingRoomRepository;
import com.freepath.devpath.chatting.exception.ChattingJoinException;
import com.freepath.devpath.chatting.exception.ChattingRoomException;
import com.freepath.devpath.chatting.query.dto.response.ChattingRoomDTO;
import com.freepath.devpath.chatting.query.dto.response.ChattingRoomResponse;
import com.freepath.devpath.chatting.query.dto.response.ChattingRoomWaitingDTO;
import com.freepath.devpath.chatting.query.dto.response.GroupChattingWaitingRoomResponse;
import com.freepath.devpath.chatting.query.mapper.ChattingRoomMapper;
import com.freepath.devpath.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChattingRoomQueryService {
    private final ChattingRoomMapper chattingRoomMapper;
    private final ChattingJoinRepository chattingJoinRepository;
    private final ChattingRoomRepository chattingRoomRepository;

    public ChattingRoomResponse getChattingRooms(String username) {
        int userId = Integer.parseInt(username);
        List<ChattingRoomDTO> chattingRooms = chattingRoomMapper.selectChattingRooms(userId);
        return ChattingRoomResponse.builder()
                .chattingRooms(chattingRooms)
                .build();
    }

    public GroupChattingWaitingRoomResponse getWaitingRoom(int chattingRoomId, String username) {
        int userId = Integer.parseInt(username);
        chattingRoomRepository.findById(chattingRoomId).orElseThrow(
                () -> new ChattingRoomException(ErrorCode.NO_SUCH_CHATTING_ROOM)
        );
        ChattingJoin chattingJoin = chattingJoinRepository.findById(new ChattingJoinId(chattingRoomId,userId)).orElseThrow(
                ()-> new ChattingJoinException(ErrorCode.NO_CHATTING_JOIN)
        );
        if(!chattingJoin.getChattingRole().equals(ChattingRole.OWNER)){
            throw new ChattingJoinException(ErrorCode.NO_CHATTING_ROOM_AUTH);
        }
        List<ChattingRoomWaitingDTO> chattingRoomWaitingDTOS = chattingRoomMapper.selectWaitingUsers(chattingRoomId);

        return GroupChattingWaitingRoomResponse.builder()
                .chattingRoomWatingDTOList(chattingRoomWaitingDTOS)
                .build();

    }
}
