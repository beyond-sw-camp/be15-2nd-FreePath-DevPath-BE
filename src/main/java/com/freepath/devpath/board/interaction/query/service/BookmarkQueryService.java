package com.freepath.devpath.board.interaction.query.service;


import com.freepath.devpath.board.interaction.query.mapper.BookmarkQueryMapper;
import com.freepath.devpath.board.post.query.dto.response.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkQueryService {
    private final BookmarkQueryMapper bookmarkQueryMapper;

    public List<PostDto> getBookmarkedPosts(int userId) {
        return bookmarkQueryMapper.selectBookmarkedPostListByUserId(userId);
    }
}