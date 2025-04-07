package com.freepath.devpath.chattingroom.command.application.service;

import com.freepath.devpath.chattingroom.command.application.dto.response.ChattingRoomCommandResponse;
import com.freepath.devpath.chattingroom.command.domain.aggregate.ChattingJoin;
import com.freepath.devpath.chattingroom.command.domain.aggregate.ChattingJoinId;
import com.freepath.devpath.chattingroom.command.domain.aggregate.ChattingRole;
import com.freepath.devpath.chattingroom.command.domain.aggregate.ChattingRoom;
import com.freepath.devpath.chattingroom.command.domain.repository.ChattingJoinRepository;
import com.freepath.devpath.chattingroom.command.domain.repository.ChattingRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChattingRoomCommandService {
    private final ChattingJoinRepository chattingJoinRepository;
    private final ChattingRoomRepository chattingRoomRepository;

    @Transactional
    public ChattingRoomCommandResponse createChattingRoom(
            String username,
            int inviteeId
    ) {
        ChattingRoom chattingRoom = ChattingRoom.builder()
                .userCount(2)
                .chattingRoomTitle("채팅방")
                .boardId(null)
                .build();
        ChattingRoom savedChattingRoom = chattingRoomRepository.save(chattingRoom);
        int chattingRoomId = savedChattingRoom.getChattingRoomId();
        int creatorId = Integer.parseInt(username);
        ChattingJoin chattingJoin1 = chattingJoinBuild(creatorId, chattingRoomId,ChattingRole.ONE);
        ChattingJoin chattingJoin2 = chattingJoinBuild(inviteeId, chattingRoomId,ChattingRole.ONE);

        chattingJoinRepository.save(chattingJoin1);
        chattingJoinRepository.save(chattingJoin2);
        return new ChattingRoomCommandResponse(chattingRoomId);
    }


    private ChattingJoin chattingJoinBuild(int userId, int chattingRoomId, ChattingRole chattingRole) {
        return ChattingJoin.builder()
                        .id(new ChattingJoinId(chattingRoomId, userId))
                .chattingRole(chattingRole)
                .chattingJoinStatus('Y')
                                .build();

    }

    @Transactional
    public ChattingRoomCommandResponse createGroupChattingRoom(
            String username, int boardId
    ) {
        ChattingRoom chattingRoom = ChattingRoom.builder()
                .userCount(1)
                .chattingRoomTitle("채팅방")
                .boardId(boardId)
                .build();
        ChattingRoom savedChattingRoom = chattingRoomRepository.save(chattingRoom);
        int chattingRoomId = savedChattingRoom.getChattingRoomId();
        int creatorId = Integer.parseInt(username);
        ChattingJoin chattingJoin1 = chattingJoinBuild(creatorId, chattingRoomId,ChattingRole.OWNER);


        chattingJoinRepository.save(chattingJoin1);
        return new ChattingRoomCommandResponse(chattingRoomId);
    }

    @Transactional
    public void quitChattingRoom(String username, int chattingRoomId) {
        int userId = Integer.parseInt(username);
        ChattingRoom chattingRoom = chattingRoomRepository.findById(chattingRoomId)
                .orElseThrow(() -> new RuntimeException("해당 채팅방은 없습니다."));
        chattingRoom.setUserCount(chattingRoom.getUserCount()-1);
        ChattingJoin chattingJoin= chattingJoinRepository.findById(new ChattingJoinId(chattingRoomId,userId))
                .orElseThrow(() -> new RuntimeException("채팅방에 참여하고 있지 않습니다."));
        chattingJoin.setChattingJoinStatus('N');

    }

    @Transactional
    public void deleteChattingRoom(String username, int chattingRoomId) {
        int userId = Integer.parseInt(username);
        chattingRoomRepository.deleteById(chattingRoomId);
        chattingJoinRepository.deleteByChattingRoomId(chattingRoomId);
    }
}
