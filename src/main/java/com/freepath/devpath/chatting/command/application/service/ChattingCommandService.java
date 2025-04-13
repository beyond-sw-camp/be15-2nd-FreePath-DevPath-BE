package com.freepath.devpath.chatting.command.application.service;

import com.freepath.devpath.chatting.command.application.dto.response.ChattingDto;
import com.freepath.devpath.chatting.command.domain.repository.ChattingRepository;
import com.freepath.devpath.chatting.command.domain.aggregate.ChatDTO;
import com.freepath.devpath.chatting.command.domain.aggregate.Chatting;
import com.freepath.devpath.chatting.command.domain.repository.ChattingRoomRepository;
import com.freepath.devpath.chatting.exception.ChattingRoomException;
import com.freepath.devpath.chatting.exception.InvalidMessageException;
import com.freepath.devpath.common.exception.ErrorCode;
import com.freepath.devpath.user.command.entity.User;
import com.freepath.devpath.user.command.repository.UserCommandRepository;
import com.freepath.devpath.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChattingCommandService {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChattingRepository chattingRepository;
    private final ChattingRoomRepository chattingRoomRepository;
    private final UserCommandRepository userCommandRepository;
    private final ChattingJoinCommandService chattingJoinCommandService;


    @Transactional
    public void sendChatting(ChatDTO chatDTO, int userId) {
        //유효성 검사
        //유효한 채팅방인지
        if(chatDTO.getChattingRoomId()!=null && !chattingRoomRepository.existsById(chatDTO.getChattingRoomId())){
            throw new ChattingRoomException(ErrorCode.NO_SUCH_CHATTING_ROOM);
        }
        //채팅방에 참여중인 유저인지
        chattingJoinCommandService.checkStatus(chatDTO.getChattingRoomId(),userId);
        //유효한 메세지인지
        if(chatDTO.getChattingMessage()==null){
            throw new InvalidMessageException(ErrorCode.INVALID_MESSAGE);
        }
        send(chatDTO, userId);
    }

    @Transactional
    public void sendSystemMessage(int chattingRoomId, String message){
        send(ChatDTO.builder()
                .chattingMessage(message)
                .chattingRoomId(chattingRoomId)
                .build(),1);
    }


    @Transactional
    public void send(ChatDTO chatDTO, int userId) {
        Chatting chatting = Chatting.builder()
                .chattingRoomId(chatDTO.getChattingRoomId())
                .userId(userId)
                .chattingMessage(chatDTO.getChattingMessage())
                .chattingCreatedAt(LocalDateTime.now())
                .build();

        Chatting savedChatting = chattingRepository.save(chatting);
        User user = userCommandRepository.findById((long) userId).orElseThrow(
                () -> new UserException(ErrorCode.USER_NOT_FOUND)
        );

        ChattingDto chattingDto = ChattingDto.builder()
                .message(savedChatting.getChattingMessage())
                .timestamp(savedChatting.getChattingCreatedAt().toString())
                .nickname(user.getNickname())
                        .build();
        messagingTemplate.convertAndSend("/topic/room/" + chatting.getChattingRoomId(), chattingDto);
    }
}
