package com.freepath.devpath.board.interaction.command.application.service;

import com.freepath.devpath.board.comment.command.domain.repository.CommentRepository;
import com.freepath.devpath.board.interaction.command.application.dto.*;
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
        if (request instanceof BoardLikeRequest boardReq) {
            Long boardId = boardReq.getBoardId();
            if (!postRepository.existsById(boardId.intValue())) {
                throw new BoardNotFoundException(ErrorCode.POST_NOT_FOUND);
            }

            if (likeRepository.existsByUserIdAndBoardId(userId, boardId)) {
                throw new AlreadyLikedException(ErrorCode.ALREADY_LIKED);
            }

            likeRepository.save(Like.builder()
                    .userId(userId)
                    .boardId(boardId)
                    .commentId(null)
                    .build());

        } else if (request instanceof CommentLikeRequest commentReq) {
            Long commentId = commentReq.getCommentId();
            if (!commentRepository.existsById(commentId.intValue())) {
                throw new CommentNotFoundException(ErrorCode.COMMENT_NOT_FOUND);
            }

            if (likeRepository.existsByUserIdAndCommentId(userId, commentId)) {
                throw new AlreadyLikedException(ErrorCode.ALREADY_LIKED);
            }

            likeRepository.save(Like.builder()
                    .userId(userId)
                    .commentId(commentId)
                    .boardId(null)
                    .build());

        } else {
            throw new InvalidLikeTargetException(ErrorCode.INVALID_LIKE_TARGET);
        }
    }

    @Transactional
    public void unlike(Long userId, LikeRequest request) {
        if (request instanceof BoardLikeRequest boardReq) {
            Long boardId = boardReq.getBoardId();
            if (!postRepository.existsById(boardId.intValue())) {
                throw new BoardNotFoundException(ErrorCode.POST_NOT_FOUND);
            }

            if (!likeRepository.existsByUserIdAndBoardId(userId, boardId)) {
                throw new LikeNotFoundException(ErrorCode.LIKE_NOT_FOUND);
            }

            likeRepository.deleteByUserIdAndBoardId(userId, boardId);

        } else if (request instanceof CommentLikeRequest commentReq) {
            Long commentId = commentReq.getCommentId();
            if (!commentRepository.existsById(commentId.intValue())) {
                throw new CommentNotFoundException(ErrorCode.COMMENT_NOT_FOUND);
            }

            if (!likeRepository.existsByUserIdAndCommentId(userId, commentId)) {
                throw new LikeNotFoundException(ErrorCode.LIKE_NOT_FOUND);
            }

            likeRepository.deleteByUserIdAndCommentId(userId, commentId);

        } else {
            throw new InvalidLikeTargetException(ErrorCode.INVALID_LIKE_TARGET);
        }
    }
}
