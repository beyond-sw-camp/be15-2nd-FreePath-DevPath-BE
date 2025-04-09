package com.freepath.devpath.chatting.command.application.service;

import com.freepath.devpath.chatting.command.application.dto.response.ChattingResponse;
import com.freepath.devpath.chatting.command.application.dto.response.ChattingRoomCommandResponse;
import com.freepath.devpath.chatting.command.domain.aggregate.*;
import com.freepath.devpath.chatting.command.domain.repository.ChattingJoinRepository;
import com.freepath.devpath.chatting.command.domain.repository.ChattingRepository;
import com.freepath.devpath.chatting.command.domain.repository.ChattingRoomRepository;
import com.freepath.devpath.chatting.exception.NoChattingJoinException;
import com.freepath.devpath.chatting.exception.NoSuchChattingRoomException;
import com.freepath.devpath.common.exception.ErrorCode;
import com.freepath.devpath.user.command.entity.User;
import com.freepath.devpath.user.command.repository.UserCommandRepository;
import com.freepath.devpath.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChattingRoomCommandService {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChattingJoinRepository chattingJoinRepository;
    private final ChattingRoomRepository chattingRoomRepository;
    private final UserCommandRepository userCommandRepository;
    private final ChattingRepository chattingRepository;

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
        //유효성 검사
        ChattingRoom chattingRoom = chattingRoomRepository.findById(chattingRoomId)
                .orElseThrow(() -> new NoSuchChattingRoomException(ErrorCode.NO_SUCH_CHATTING_ROOM));
        chattingRoom.setUserCount(chattingRoom.getUserCount()-1);
        ChattingJoin chattingJoin= chattingJoinRepository.findById(new ChattingJoinId(chattingRoomId,userId))
                .orElseThrow(() -> new NoChattingJoinException(ErrorCode.NO_CHATTING_JOIN));
        //퇴장 처리
        chattingJoin.setChattingJoinStatus('N');
        User user = userCommandRepository.findById((long)userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        //채팅 생성
        Chatting chatting = Chatting.builder()
                .chattingRoomId(chattingRoomId)
                .userId(userId)
                .chattingMessage(user.getNickname()+"님이 퇴장했습니다.")
                .chattingCreatedAt(LocalDateTime.now())
                .build();
        Chatting savedChatting = chattingRepository.save(chatting);
        //메세지 처리
        ChattingResponse chattingResponse = ChattingResponse.builder()
                .message(savedChatting.getChattingMessage())
                .timestamp(savedChatting.getChattingCreatedAt().toString())
                .nickname(user.getNickname())
                .build();
        messagingTemplate.convertAndSend("/topic/room/" + chattingRoomId, chattingResponse);

    }

    @Transactional
    public void deleteChattingRoom(String username, int chattingRoomId) {
        int userId = Integer.parseInt(username);
        chattingRoomRepository.deleteById(chattingRoomId);
        chattingJoinRepository.deleteByChattingRoomId(chattingRoomId);
    }

    public boolean isChattingJoin(int userId, int chattingRoomId) {
        return chattingJoinRepository.findById(new ChattingJoinId(chattingRoomId, userId)).isPresent();
    }

    public boolean isChattingRoomExists(int roomId) {
        return chattingRoomRepository.findById(roomId).isPresent();
    }
}
