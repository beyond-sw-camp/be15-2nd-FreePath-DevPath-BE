package com.freepath.devpath.common.jwt;

import com.freepath.devpath.chatting.command.application.service.ChattingRoomCommandService;
import com.freepath.devpath.chatting.command.application.service.UserBlockCommandService;
import com.freepath.devpath.chatting.command.domain.repository.ChattingJoinRepository;
import com.freepath.devpath.chatting.query.service.ChattingRoomQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
@Slf4j
@RequiredArgsConstructor
public class StompAuthChannelInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final ChattingRoomCommandService chattingRoomCommandService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor
                .getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand()) ||
                StompCommand.SEND.equals(accessor.getCommand())
        ) {
            String token = accessor.getFirstNativeHeader("Authorization");

            //유효하지 않은 토큰인 경우
            if (token == null || !jwtTokenProvider.validateToken(token)) {
                throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
            }
            int userId = Integer.parseInt(jwtTokenProvider.getUsernameFromJWT(token));
            Principal principal = () -> Integer.toString(userId);
            accessor.setUser(principal);
        }

        //String destination = accessor.getDestination(); // "/topic/chat/123"
        //            int roomId = extractRoomId(destination);
        //
        //            //만약 채팅방에 참여중인 사용자가 아니라면
        //            if(!chattingRoomCommandService.isChattingJoin(userId,roomId)){
        //                throw new RuntimeException("채팅방에 참여중이 아닙니다.");
        //            }
        //            if(!chattingRoomCommandService.isChattingRoomExists(roomId)){
        //                throw new RuntimeException("채팅방이 존재하지 않습니다.");
        //            }



        return message;
    }

    private int extractRoomId(String destination) {
        // "/topic/chat/123" -> 123
        String[] parts = destination.split("/");
        return Integer.parseInt(parts[parts.length - 1]);
    }
}

