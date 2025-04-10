package com.freepath.devpath.board.interaction.query.service;


import com.freepath.devpath.board.interaction.query.mapper.LikeQueryMapper;
import com.freepath.devpath.board.post.query.dto.response.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeQueryService {
    private final LikeQueryMapper likeQueryMapper;


    public List<PostDto> getLikedPosts(int userId) {
        return likeQueryMapper.selectLikedPostListByUserId(userId);
    }
}
