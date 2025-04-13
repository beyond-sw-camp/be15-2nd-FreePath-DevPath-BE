package com.freepath.devpath.chatting.command.application.service;

import com.freepath.devpath.chatting.command.domain.jpa.aggregate.ChattingJoin;
import com.freepath.devpath.chatting.command.domain.jpa.aggregate.ChattingJoinId;
import com.freepath.devpath.chatting.command.domain.jpa.aggregate.ChattingRole;
import com.freepath.devpath.chatting.command.domain.jpa.repository.ChattingJoinRepository;
import com.freepath.devpath.chatting.exception.ChattingJoinException;
import com.freepath.devpath.chatting.exception.ChattingRoomException;
import com.freepath.devpath.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

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
    @Transactional
    public ChattingJoin findById(ChattingJoinId chattingJoinId) {
        return chattingJoinRepository.findById(chattingJoinId)
                .orElseThrow(() -> new ChattingJoinException(ErrorCode.NO_CHATTING_JOIN));
    }
    @Transactional
    public void setQuit(int chattingRoomId, int userId) {
        ChattingJoin chattingJoin = chattingJoinRepository.findById(new ChattingJoinId(chattingRoomId,userId))
                .orElseThrow(() -> new ChattingJoinException(ErrorCode.NO_CHATTING_JOIN));
        if(chattingJoin.getChattingJoinStatus()!='Y')
            throw new ChattingJoinException(ErrorCode.NO_CHATTING_JOIN);
        chattingJoin.setChattingJoinStatus('N');
        if(chattingJoin.getChattingRole()== ChattingRole.OWNER){
            chattingJoin.setChattingRole(ChattingRole.MEMBER);
        }
    }
    @Transactional
    public void insert(int userId, int chattingRoomId, ChattingRole chattingRole,char joinStatus) {
        ChattingJoin chattingJoin = ChattingJoin.builder()
                .id(new ChattingJoinId(chattingRoomId, userId))
                .chattingRole(chattingRole)
                .chattingJoinStatus(joinStatus)
                .build();
        chattingJoinRepository.save(chattingJoin);
    }

    @Transactional
    public void checkOwner(int chattingRoomId, int userId) {
        ChattingJoin chattingJoin = chattingJoinRepository.findById(new ChattingJoinId(chattingRoomId,userId))
                .orElseThrow(
                        () -> new ChattingJoinException(ErrorCode.NO_CHATTING_JOIN)
                );
        if(chattingJoin.getChattingRole()!=ChattingRole.OWNER || chattingJoin.getChattingJoinStatus()!='Y'){
            throw new ChattingRoomException(ErrorCode.NO_CHATTING_ROOM_AUTH);
        }
    }

    @Transactional
    public void deleteByChattingRoomId(int chattingRoomId) {
        chattingJoinRepository.deleteByChattingRoomId(chattingRoomId);
    }

    @Transactional
    public void checkStatus(int chattingRoomId, int userId) {
        Optional<ChattingJoin> join = chattingJoinRepository.findById(new ChattingJoinId(chattingRoomId,userId));
        if(join.isEmpty() || join.get().getChattingJoinStatus()!='Y'){
            throw new ChattingJoinException(ErrorCode.NO_CHATTING_JOIN);
        }
    }

    @Transactional
    public void checkWaitingStatus(int chattingRoomId, int userId){
        Optional<ChattingJoin> join = chattingJoinRepository.findById(new ChattingJoinId(chattingRoomId,userId));
        if(join.isEmpty() || join.get().getChattingJoinStatus()!='W'){
            throw new ChattingJoinException(ErrorCode.NO_CHATTING_JOIN);
        }
    }

    @Transactional
    public void setWaitingStatus(int chattingRoomId, int userId) {
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
    public void setStatus(int chattingRoomId, int userId, char joinStatus){
        ChattingJoin chattingJoin = chattingJoinRepository
                .findById(new ChattingJoinId(chattingRoomId,userId)).orElseThrow(
                        () -> new ChattingJoinException(ErrorCode.NO_CHATTING_JOIN)
                );
        chattingJoin.setChattingJoinStatus(joinStatus);
    }
}
