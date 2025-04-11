package com.freepath.devpath.chatting.command.application.service;

import com.freepath.devpath.chatting.command.domain.aggregate.ChattingJoin;
import com.freepath.devpath.chatting.command.domain.aggregate.ChattingJoinId;
import com.freepath.devpath.chatting.command.domain.repository.ChattingJoinRepository;
import com.freepath.devpath.chatting.exception.ChattingJoinException;
import com.freepath.devpath.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ChattingJoinCommandService {
    private final ChattingJoinRepository chattingJoinRepository;

    @Transactional
    public void isUserAllowedToSubscribe(int userId, String destination) {
        int chattingRoomId =  Integer.parseInt(StringUtils.getFilename(destination));
        ChattingJoin chattingJoin = chattingJoinRepository.
                findById(new ChattingJoinId(chattingRoomId,userId))
                .orElseThrow(
                        () -> new ChattingJoinException(ErrorCode.NO_CHATTING_JOIN)
                );
        if(chattingJoin.getChattingJoinStatus()!='Y')
            throw new ChattingJoinException(ErrorCode.NO_CHATTING_JOIN);

    }
}
