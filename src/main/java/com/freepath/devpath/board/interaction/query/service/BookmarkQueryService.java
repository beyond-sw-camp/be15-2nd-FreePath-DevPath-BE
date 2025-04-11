package com.freepath.devpath.board.interaction.query.service;

import com.freepath.devpath.board.interaction.query.dto.BookmarkedBoardSearchRequest;
import com.freepath.devpath.board.interaction.query.mapper.BookmarkQueryMapper;
import com.freepath.devpath.board.post.query.dto.response.PostDto;
import com.freepath.devpath.board.post.query.dto.response.PostListResponse;
import com.freepath.devpath.common.dto.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkQueryService {

    private final BookmarkQueryMapper bookmarkQueryMapper;

    @Transactional(readOnly = true)
    public PostListResponse getBookmarkedPosts(int userId, BookmarkedBoardSearchRequest request) {
        List<PostDto> posts = bookmarkQueryMapper.selectBookmarkedPostListByUserId(
                userId, request.getOffset(), request.getLimit());

        long totalItems = bookmarkQueryMapper.countBookmarksByUserId(userId);
        int page = request.getPage();
        int size = request.getSize();

        return PostListResponse.builder()
                .posts(posts)
                .pagination(Pagination.builder()
                        .currentPage(page)
                        .totalPage((int) Math.ceil((double) totalItems / size))
                        .totalItems(totalItems)
                        .build())
                .build();
    }



    public long getTotalBookmarks(int userId) {
        return bookmarkQueryMapper.countBookmarksByUserId(userId);
    }
}
