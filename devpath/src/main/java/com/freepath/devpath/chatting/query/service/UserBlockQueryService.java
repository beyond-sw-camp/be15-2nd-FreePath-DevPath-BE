package com.freepath.devpath.chatting.query.service;

import com.freepath.devpath.chatting.query.dto.response.UserBlockDTO;
import com.freepath.devpath.chatting.query.dto.response.UserBlockResponse;
import com.freepath.devpath.chatting.query.mapper.UserBlockMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserBlockQueryService {
    private final UserBlockMapper userBlockMapper;

    @Transactional
    public UserBlockResponse getUserBlocked(String username) {
        int userId = Integer.parseInt(username);
        List<UserBlockDTO> userBlockDTOS = userBlockMapper.selectUserBlocks(userId);
        return UserBlockResponse.builder()
                .userBlockDTOList(userBlockDTOS)
                .build();
    }


}
