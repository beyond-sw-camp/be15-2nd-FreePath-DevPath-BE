package com.freepath.devpath.chatting.query.service;

import com.freepath.devpath.chatting.command.application.dto.response.ChattingListResponse;
import com.freepath.devpath.chatting.command.application.service.ChattingJoinCommandService;
import com.freepath.devpath.chatting.command.domain.jpa.repository.ChattingRoomRepository;
import com.freepath.devpath.chatting.exception.ChattingRoomException;
import com.freepath.devpath.chatting.query.dto.response.ChattingDTO;
import com.freepath.devpath.chatting.query.mapper.ChattingMapper;
import com.freepath.devpath.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChattingQueryService {
    private final ChattingMapper chattingMapper;
    private final ChattingRoomRepository chattingRoomRepository;
    private final ChattingJoinCommandService chattingJoinCommandService;


    @Transactional(readOnly = true)
    public ChattingListResponse getChattingList(int userId, int chattingRoomId) {
        //유효성 체크
        chattingRoomRepository.findById(chattingRoomId).orElseThrow(
                () -> new ChattingRoomException(ErrorCode.NO_SUCH_CHATTING_ROOM)
        );
        chattingJoinCommandService.checkStatus(chattingRoomId, userId);

        List<ChattingDTO> chattingDTOList = chattingMapper.selectChattings(chattingRoomId);

        return new ChattingListResponse(chattingDTOList);
    }


}
