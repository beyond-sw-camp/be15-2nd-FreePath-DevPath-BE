package com.freepath.devpath.board.interaction.query.service;

import com.freepath.devpath.board.comment.command.repository.CommentRepository;
import com.freepath.devpath.board.interaction.exception.BoardNotFoundException;
import com.freepath.devpath.board.interaction.exception.CommentNotFoundException;
import com.freepath.devpath.board.interaction.query.dto.LikedBoardSearchRequest;
import com.freepath.devpath.board.interaction.query.mapper.LikeQueryMapper;
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
public class LikeQueryService {
    private final LikeQueryMapper likeQueryMapper;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public PostListResponse getLikedPosts(int userId, LikedBoardSearchRequest request) {
        List<PostDto> likedPosts = likeQueryMapper.selectLikedPostListByUserId(userId, request.getOffset(), request.getLimit());
        long totalItems = likeQueryMapper.countLikedPostsByUserId(userId);

        int page = request.getPage();
        int size = request.getSize();
        int totalPage = (int) Math.ceil((double) totalItems / size);

        return PostListResponse.builder()
                .posts(likedPosts)
                .pagination(Pagination.builder()
                        .currentPage(page)
                        .totalPage(totalPage)
                        .totalItems(totalItems)
                        .build())
                .build();
    }

    @Transactional(readOnly = true)
    public boolean hasUserLikedBoard(int userId, int boardId) {
        if (!postRepository.existsById(boardId)) {
            throw new BoardNotFoundException(ErrorCode.POST_NOT_FOUND);
        }
        return likeQueryMapper.hasUserLikedBoard(userId, boardId);
    }

    @Transactional(readOnly = true)
    public boolean hasUserLikedComment(int userId, int commentId) {

        if (!commentRepository.existsById(commentId)) {
            throw new CommentNotFoundException(ErrorCode.COMMENT_NOT_FOUND);
        }

        return likeQueryMapper.hasUserLikedComment(userId, commentId);
    }


    @Transactional(readOnly = true)
    public int countLikesByBoardId(int boardId) {
        if (!postRepository.existsById(boardId)) {
            throw new BoardNotFoundException(ErrorCode.POST_NOT_FOUND);
        }
        return likeQueryMapper.countLikesByBoardId(boardId);
    }

    @Transactional(readOnly = true)
    public int countLikesByCommentId(int commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new CommentNotFoundException(ErrorCode.COMMENT_NOT_FOUND);
        }
        return likeQueryMapper.countLikesByCommentId(commentId);
    }


}
