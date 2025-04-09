package com.freepath.devpath.chatting.command.application.controller;

import com.freepath.devpath.chatting.command.application.service.ChattingCommandService;
import com.freepath.devpath.chatting.command.domain.aggregate.ChatDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChattingCommandController {
    //@MessageMapping()의 경로가 "/chat/message"이지만 ChatConfig의 setApplicationDestinationPrefixes()를 통해
    // prefix를 "/app"으로 해줬기 때문에 실질 경로는 "/app/chat/message"가 됨
    //클라이언트에서 "/app/chat/message"의 경로로 메시지를 보내는 요청을 하면,
    //메시지 Controller가 받아서 "topic/chat/room/{roomId}"를 구독하고 있는 클라이언트에게 메시지를 전달하게 됨.

    private final ChattingCommandService chattingService;
    //실제 요청은 app이 선행되어야 한다.
    @MessageMapping("/chatting/send")
    public void send(ChatDTO chatDTO, Principal principal) {
        log.info(principal.getName()+" : "+chatDTO.toString());
        chattingService.sendChatting(chatDTO,principal);
    }
}
