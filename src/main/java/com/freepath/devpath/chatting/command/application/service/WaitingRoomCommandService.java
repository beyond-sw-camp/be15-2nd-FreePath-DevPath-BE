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
    private final ChattingRoomRepository chattingRoomRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserCommandRepository userCommandRepository;
    private final ChattingRepository chattingRepository;
    private final ChattingJoinCommandService chattingJoinCommandService;
    @Transactional
    public void joinRequest(int chattingRoomId, String username) {
        int userId = Integer.parseInt(username);

        ChattingRoom chattingRoom = chattingRoomRepository.findById(chattingRoomId).orElseThrow(
                () -> new ChattingRoomException(ErrorCode.NO_SUCH_CHATTING_ROOM)
        );
        if(chattingRoom.getBoardId() == null){
            throw new ChattingRoomException(ErrorCode.NO_SUCH_CHATTING_ROOM);
        }
        chattingJoinCommandService.setChattingJoinStatus(chattingRoomId,userId,'W');
    }
    @Transactional
    public void requestRespond(WaitingRoomActionRequest request, String username) {
        int userId = Integer.parseInt(username);
        int chattingRoomId = request.getChattingRoomId();
        int inviteeId = request.getInviteeId();
        //방장 권한 조회
        chattingJoinCommandService.checkOwner(chattingRoomId,userId);
        ChattingRoom chattingRoom = chattingRoomRepository.findById(chattingRoomId)
                .orElseThrow(
                        (() -> new ChattingRoomException(ErrorCode.NO_SUCH_CHATTING_ROOM))
                );
        // 참여 요청상태 확인
        chattingJoinCommandService.checkWatingStatus(chattingRoomId, inviteeId);
        //요청 수락
        String message;
        if(request.getAction().equals(WaitingRoomAction.ACCEPT)){
            chattingJoinCommandService.setChattingJoinStatus(chattingRoomId, inviteeId, 'Y');
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
            chattingJoinCommandService.setChattingJoinStatus(chattingRoomId, inviteeId, 'N');
        }

    }
}
