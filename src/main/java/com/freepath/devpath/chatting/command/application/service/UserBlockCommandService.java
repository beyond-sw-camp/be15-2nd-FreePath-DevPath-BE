package com.freepath.devpath.chatting.command.application.service;

import com.freepath.devpath.chatting.command.domain.aggregate.UserBlock;
import com.freepath.devpath.chatting.command.domain.repository.UserBlockRepository;
import jakarta.persistence.TableGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserBlockCommandService {
    private final UserBlockRepository userBlockRepository;
    @Transactional
    public void blockUser(String username, int blockedId) {
        int blockerId = Integer.parseInt(username);
        Optional<UserBlock> existing = userBlockRepository
                .findByBlockerIdAndBlockedId(blockerId, blockedId);

        if (existing.isPresent()) {
            throw new IllegalStateException("이미 차단한 사용자입니다.");
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

        if (selectUserBlock.isEmpty()) {
            throw new IllegalStateException("차단한 사용자가 존재하지 않습니다.");
        }

        userBlockRepository.delete(selectUserBlock.get());
    }
}
