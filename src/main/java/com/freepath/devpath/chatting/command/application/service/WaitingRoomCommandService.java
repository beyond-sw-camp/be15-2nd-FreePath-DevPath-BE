package com.freepath.devpath.chatting.command.application.service;

import com.freepath.devpath.chatting.command.application.dto.request.WaitingRoomActionRequest;
import com.freepath.devpath.chatting.command.application.dto.request.WaitingRoomAction;
import com.freepath.devpath.chatting.command.application.dto.response.ChattingResponse;
import com.freepath.devpath.chatting.command.domain.aggregate.*;
import com.freepath.devpath.chatting.command.domain.repository.ChattingJoinRepository;
import com.freepath.devpath.chatting.command.domain.repository.ChattingRepository;
import com.freepath.devpath.chatting.command.domain.repository.ChattingRoomRepository;
import com.freepath.devpath.chatting.exception.ChattingJoinException;
import com.freepath.devpath.chatting.exception.ChattingRoomException;
import com.freepath.devpath.common.exception.ErrorCode;
import com.freepath.devpath.user.command.entity.User;
import com.freepath.devpath.user.command.repository.UserCommandRepository;
import com.freepath.devpath.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WaitingRoomCommandService {
    private final ChattingJoinRepository chattingJoinRepository;
    private final ChattingRoomRepository chattingRoomRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserCommandRepository userCommandRepository;
    private final ChattingRepository chattingRepository;

    @Transactional
    public void joinRequest(int chattingRoomId, String username) {
        int userId = Integer.parseInt(username);

        ChattingRoom chattingRoom = chattingRoomRepository.findById(chattingRoomId).orElseThrow(
                () -> new ChattingRoomException(ErrorCode.NO_SUCH_CHATTING_ROOM)
        );
        if(chattingRoom.getBoardId() == null){
            throw new ChattingRoomException(ErrorCode.NO_SUCH_CHATTING_ROOM);
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
    @Transactional
    public void requestRespond(WaitingRoomActionRequest request, String username) {
        int userId = Integer.parseInt(username);
        //방장 권한 조회
        Optional<ChattingJoin> optionalChattingJoin = chattingJoinRepository
                .findById(new ChattingJoinId(request.getChattingRoomId(),userId));
        if(optionalChattingJoin.isEmpty()){
            throw new ChattingJoinException(ErrorCode.NO_CHATTING_JOIN);
        }
        else if(!optionalChattingJoin.get().getChattingRole().equals(ChattingRole.OWNER)){
            throw new ChattingJoinException(ErrorCode.NO_CHATTING_ROOM_AUTH);
        }
        ChattingRoom chattingRoom = chattingRoomRepository.findById(request.getChattingRoomId())
                .orElseThrow(
                        ( () -> new ChattingRoomException(ErrorCode.NO_SUCH_CHATTING_ROOM))
                );
        // 참여 요청상태 확인
        ChattingJoin chattingJoin = chattingJoinRepository
                .findById(new ChattingJoinId(request.getChattingRoomId(),request.getInviteeId()))
                .orElseThrow(
                        () -> new ChattingJoinException(ErrorCode.ALREADY_CHATTING_JOIN)
                );
        if(chattingJoin.getChattingJoinStatus()!='W'){
            throw new ChattingJoinException(ErrorCode.USER_NOT_WAITING);
        }
        //요청 수락
        String message;
        if(request.getAction().equals(WaitingRoomAction.ACCEPT)){
            chattingJoin.setChattingJoinStatus('Y');
            chattingRoom.setUserCount(chattingRoom.getUserCount()+1);
            User user = userCommandRepository.findById((long)request.getInviteeId())
                    .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
            //채팅 저장 및 전송
            Chatting chatting = Chatting.builder()
                    .chattingRoomId(request.getChattingRoomId())
                    .userId(1)
                    .chattingMessage(user.getNickname()+"님이 입장했습니다.")
                    .chattingCreatedAt(LocalDateTime.now())
                    .build();
            chattingRepository.save(chatting);
            ChattingResponse chattingResponse = ChattingResponse.builder()
                    .message(chatting.getChattingMessage())
                    .timestamp(chatting.getChattingCreatedAt().toString())
                    .nickname("SYSTEM")
                    .build();
            messagingTemplate.convertAndSend("/topic/room/" +request.getChattingRoomId(), chattingResponse);
        }//요청 거절
        else if(request.getAction().equals(WaitingRoomAction.REJECT)){
            chattingJoin.setChattingJoinStatus('N');
        }

    }
}
