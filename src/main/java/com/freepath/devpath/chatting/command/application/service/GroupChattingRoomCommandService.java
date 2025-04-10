package com.freepath.devpath.chatting.command.application.service;

import com.freepath.devpath.chatting.command.domain.aggregate.ChattingJoin;
import com.freepath.devpath.chatting.command.domain.aggregate.ChattingJoinId;
import com.freepath.devpath.chatting.command.domain.aggregate.ChattingRole;
import com.freepath.devpath.chatting.command.domain.aggregate.ChattingRoom;
import com.freepath.devpath.chatting.command.domain.repository.ChattingJoinRepository;
import com.freepath.devpath.chatting.command.domain.repository.ChattingRoomRepository;
import com.freepath.devpath.chatting.exception.ChattingJoinException;
import com.freepath.devpath.chatting.exception.ChattingRoomException;
import com.freepath.devpath.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupChattingRoomCommandService {
    private final ChattingJoinRepository chattingJoinRepository;
    private final ChattingRoomRepository chattingRoomRepository;

    @Transactional
    public void joinRequest(int chattingRoomId, String username) {
        int userId = Integer.parseInt(username);

        ChattingRoom chattingRoom = chattingRoomRepository.findById(chattingRoomId).orElseThrow(
                () -> new ChattingRoomException(ErrorCode.NO_SUCH_CHATTING_ROOM)
        );
        if(chattingRoom.getBoardId() == null){
            throw new ChattingRoomException(ErrorCode.NO_SUCH_CHATTING_ROOM);
        }
        if(chattingJoinRepository.existsById(new ChattingJoinId(chattingRoomId,userId))){
            throw new ChattingJoinException(ErrorCode.ALREADY_CHATTING_JOIN);
        }
        Optional<ChattingJoin> optionalChattingJoin = chattingJoinRepository.findById(new ChattingJoinId(chattingRoomId,userId));
        if(optionalChattingJoin.isEmpty()){
            ChattingJoin chattingJoin = ChattingJoin.builder()
                    .id(new ChattingJoinId(chattingRoomId,userId))
                    .chattingRole(ChattingRole.MEMBER)
                    .chattingJoinStatus('W')
                    .build();
            chattingJoinRepository.save(chattingJoin);
        }
        else if(optionalChattingJoin.get().getChattingJoinStatus()=='N'){
            ChattingJoin chattingJoin = optionalChattingJoin.get();
            chattingJoin.setChattingJoinStatus('W');
        }
        else{
            throw new ChattingJoinException(ErrorCode.ALREADY_CHATTING_JOIN);
        }

    }
}
