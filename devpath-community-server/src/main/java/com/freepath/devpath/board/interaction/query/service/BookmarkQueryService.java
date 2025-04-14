package com.freepath.devpath.board.interaction.query.service;

import com.freepath.devpath.board.interaction.exception.BoardNotFoundException;
import com.freepath.devpath.board.interaction.query.dto.BookmarkedBoardSearchRequest;
import com.freepath.devpath.board.interaction.query.mapper.BookmarkQueryMapper;
import com.freepath.devpath.board.post.command.repository.PostRepository;
import com.freepath.devpath.board.post.query.dto.response.PostDto;
import com.freepath.devpath.board.post.query.dto.response.PostListResponse;
import com.freepath.devpath.common.dto.Pagination;
import com.freepath.devpath.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkQueryService {

    private final BookmarkQueryMapper bookmarkQueryMapper;
    private final PostRepository postRepository;

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

    @Transactional(readOnly = true)
    public boolean hasUserBookmarkedPost(int userId, int boardId) {
        if (!postRepository.existsById(boardId)) {
            throw new BoardNotFoundException(ErrorCode.POST_NOT_FOUND);
        }

        return bookmarkQueryMapper.hasUserBookmarkedPost(userId, boardId);
    }



}
