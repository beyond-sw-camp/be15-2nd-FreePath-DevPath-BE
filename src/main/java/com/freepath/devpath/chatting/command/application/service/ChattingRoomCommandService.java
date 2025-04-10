package com.freepath.devpath.chatting.command.application.service;

import com.freepath.devpath.chatting.command.application.dto.response.ChattingResponse;
import com.freepath.devpath.chatting.command.application.dto.response.ChattingRoomCommandResponse;
import com.freepath.devpath.chatting.command.domain.aggregate.*;
import com.freepath.devpath.chatting.command.domain.repository.ChattingJoinRepository;
import com.freepath.devpath.chatting.command.domain.repository.ChattingRepository;
import com.freepath.devpath.chatting.command.domain.repository.ChattingRoomRepository;
import com.freepath.devpath.chatting.exception.ChattingRoomException;
import com.freepath.devpath.chatting.exception.ChattingJoinException;
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
        int creatorId = Integer.parseInt(username);
        //이미 생성되어 있는 일대일 채팅방의 경우 예외처리
        if(chattingRoomRepository.findValidChattingRoom(creatorId, inviteeId).isPresent()){
            throw new ChattingRoomException(ErrorCode.CHATTING_ROOM_ALREADY_EXISTS);
        }
        ChattingRoom chattingRoom = ChattingRoom.builder()
                .userCount(2)
                .chattingRoomTitle("채팅방")
                .boardId(null)
                .build();
        ChattingRoom savedChattingRoom = chattingRoomRepository.save(chattingRoom);
        int chattingRoomId = savedChattingRoom.getChattingRoomId();

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
        if(chattingRoomRepository.findByBoardId(boardId).isPresent()){
            throw new ChattingRoomException(ErrorCode.CHATTING_ROOM_ALREADY_EXISTS);
        }
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
                .orElseThrow(() -> new ChattingRoomException(ErrorCode.NO_SUCH_CHATTING_ROOM));
        ChattingJoin chattingJoin= chattingJoinRepository.findByIdAndChattingJoinStatus(new ChattingJoinId(chattingRoomId,userId),new Character('N'))
                .orElseThrow(() -> new ChattingJoinException(ErrorCode.NO_CHATTING_JOIN));

        //퇴장 처리
        chattingJoin.setChattingJoinStatus('N');
        if(chattingJoin.getChattingRole()==ChattingRole.OWNER){
            chattingJoin.setChattingRole(ChattingRole.MEMBER);
        }
        User user = userCommandRepository.findById((long)userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        chattingRoom.setUserCount(chattingRoom.getUserCount()-1);
        //채팅 생성
        Chatting chatting = Chatting.builder()
                .chattingRoomId(chattingRoomId)
                .userId(1)
                .chattingMessage(user.getNickname()+"님이 퇴장했습니다.")
                .chattingCreatedAt(LocalDateTime.now())
                .build();
        Chatting savedChatting = chattingRepository.save(chatting);
        //메세지 처리
        ChattingResponse chattingResponse = ChattingResponse.builder()
                .message(savedChatting.getChattingMessage())
                .timestamp(savedChatting.getChattingCreatedAt().toString())
                .nickname("System")
                .build();
        messagingTemplate.convertAndSend("/topic/room/" + chattingRoomId, chattingResponse);

    }

    @Transactional
    public void deleteChattingRoom(String username, int chattingRoomId) {
        int userId = Integer.parseInt(username);
        ChattingRoom chattingRoom = chattingRoomRepository.findById(chattingRoomId)
                .orElseThrow(() -> new ChattingRoomException(ErrorCode.NO_SUCH_CHATTING_ROOM));
        chattingRoom.setBoardId(null);
        chattingJoinRepository.deleteByChattingRoomId(chattingRoomId);
        chattingRepository.deleteByChattingRoomId(chattingRoomId);
        chattingRoomRepository.delete(chattingRoom);
        chattingRoom.setUserCount(0);
    }
}
