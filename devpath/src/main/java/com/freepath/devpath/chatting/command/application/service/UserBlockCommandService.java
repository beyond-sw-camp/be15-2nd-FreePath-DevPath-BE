package com.freepath.devpath.chatting.command.application.service;

import com.freepath.devpath.chatting.command.domain.jpa.aggregate.UserBlock;
import com.freepath.devpath.chatting.command.domain.jpa.repository.UserBlockRepository;
import com.freepath.devpath.chatting.exception.UserAlreadyBlockedException;
import com.freepath.devpath.chatting.exception.UserNotBlockedException;
import com.freepath.devpath.common.exception.ErrorCode;
import com.freepath.devpath.user.command.entity.User;
import com.freepath.devpath.user.command.repository.UserCommandRepository;
import com.freepath.devpath.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserBlockCommandService {
    private final UserBlockRepository userBlockRepository;
    private final UserCommandRepository userCommandRepository;
    @Transactional
    public void blockUser(String username, int blockedId) {
        int blockerId = Integer.parseInt(username);
        Optional<UserBlock> existing = userBlockRepository
                .findByBlockerIdAndBlockedId(blockerId, blockedId);

        //이미 존재하는 차단인 경우
        if (existing.isPresent()) {
            throw new UserAlreadyBlockedException(ErrorCode.USER_ALREADY_BLOCKED);
        }
        //존재하지 않는 사용자를 차단하는 경우
        Optional<User> targetUser = userCommandRepository.findById((long)blockedId);
        if (targetUser.isEmpty()) {
            throw new UserException(ErrorCode.USER_NOT_FOUND);
        }

        UserBlock userBlock = UserBlock.builder()
                        .blockerId(blockerId)
                        .blockedId(blockedId)
                        .build();
        userBlockRepository.save(userBlock);

    }

    @Transactional
    public void unblockUser(String username, int blockedId) {
        int blockerId = Integer.parseInt(username);
        Optional<UserBlock> selectUserBlock = userBlockRepository.findByBlockerIdAndBlockedId(blockerId, blockedId);

        //존재하지 않는 차단을 취소하는 경우
        if (selectUserBlock.isEmpty()) {
            throw new UserNotBlockedException(ErrorCode.USER_NOT_BLOCKED);
        }

        userBlockRepository.delete(selectUserBlock.get());
    }
}
