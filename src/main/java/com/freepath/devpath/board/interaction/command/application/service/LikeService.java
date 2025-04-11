package com.freepath.devpath.board.interaction.command.application.service;

import com.freepath.devpath.board.comment.command.domain.repository.CommentRepository;
import com.freepath.devpath.board.interaction.command.application.dto.LikeRequest;
import com.freepath.devpath.board.interaction.command.domain.aggregate.Like;
import com.freepath.devpath.board.interaction.command.domain.repository.LikeRepository;
import com.freepath.devpath.board.interaction.exception.*;
import com.freepath.devpath.board.post.command.repository.PostRepository;
import com.freepath.devpath.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void like(Long userId, LikeRequest request) {
        if (request.getBoardId() == null && request.getCommentId() == null) {
            throw new InvalidLikeTargetException(ErrorCode.INVALID_LIKE_TARGET);
        }

        if (request.getBoardId() != null && request.getCommentId() != null) {
            throw new CannotLikeBothException(ErrorCode.CANNOT_LIKE_BOTH);
        }

        if (request.getBoardId() != null) {
            if (!postRepository.existsById(request.getBoardId().intValue())) {
                throw new BoardNotFoundException(ErrorCode.POST_NOT_FOUND);
            }

            if (likeRepository.existsByUserIdAndBoardId(userId, request.getBoardId())) {
                throw new AlreadyLikedException(ErrorCode.ALREADY_LIKED);
            }

            likeRepository.save(Like.builder()
                    .userId(userId)
                    .boardId(request.getBoardId())
                    .commentId(null)
                    .build());

        } else {
            if (!commentRepository.existsById(request.getCommentId().intValue())) {
                throw new CommentNotFoundException(ErrorCode.COMMENT_NOT_FOUND);
            }

            if (likeRepository.existsByUserIdAndCommentId(userId, request.getCommentId())) {
                throw new AlreadyLikedException(ErrorCode.ALREADY_LIKED);
            }

            likeRepository.save(Like.builder()
                    .userId(userId)
                    .commentId(request.getCommentId())
                    .boardId(null)
                    .build());
        }

    }

    @Transactional
    public void unlike(Long userId, LikeRequest request) {
        if (request.getBoardId() == null && request.getCommentId() == null) {
            throw new InvalidLikeTargetException(ErrorCode.INVALID_LIKE_TARGET);
        }

        if (request.getBoardId() != null && request.getCommentId() != null) {
            throw new CannotLikeBothException(ErrorCode.CANNOT_LIKE_BOTH);
        }

        if (request.getBoardId() != null) {
            if (!postRepository.existsById(request.getBoardId().intValue())) {
                throw new BoardNotFoundException(ErrorCode.POST_NOT_FOUND);
            }

            boolean exists = likeRepository.existsByUserIdAndBoardId(userId, request.getBoardId());
            if (!exists) {
                throw new LikeNotFoundException(ErrorCode.LIKE_NOT_FOUND);
            }

            likeRepository.deleteByUserIdAndBoardId(userId, request.getBoardId());

        } else {
            if (!commentRepository.existsById(request.getCommentId().intValue())) {
                throw new CommentNotFoundException(ErrorCode.COMMENT_NOT_FOUND);
            }

            boolean exists = likeRepository.existsByUserIdAndCommentId(userId, request.getCommentId());
            if (!exists) {
                throw new LikeNotFoundException(ErrorCode.LIKE_NOT_FOUND);
            }

            likeRepository.deleteByUserIdAndCommentId(userId, request.getCommentId());
        }
    }
}
