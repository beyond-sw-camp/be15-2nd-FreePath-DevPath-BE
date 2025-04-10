package com.freepath.devpath.board.interaction.command.application.service;


import com.freepath.devpath.board.interaction.command.application.dto.LikeRequest;
import com.freepath.devpath.board.interaction.command.domain.aggregate.Like;
import com.freepath.devpath.board.interaction.command.domain.repository.LikeRepository;
import com.freepath.devpath.board.interaction.exception.CannotLikeBothException;
import com.freepath.devpath.board.interaction.exception.InvalidLikeTargetException;
import com.freepath.devpath.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;

    @Transactional
    public void like(Long userId, LikeRequest request) {
        if (request.getBoardId() == null && request.getCommentId() == null) {
            throw new InvalidLikeTargetException(ErrorCode.INVALID_LIKE_TARGET);
        }

        if (request.getBoardId() != null && request.getCommentId() != null) {
            throw new CannotLikeBothException(ErrorCode.CANNOT_LIKE_BOTH);
        }

        if (request.getBoardId() != null) {
            boolean exists = likeRepository.existsByUserIdAndBoardId(userId, request.getBoardId());
            if (!exists) {
                likeRepository.save(Like.builder()
                        .userId(userId)
                        .boardId(request.getBoardId())
                        .commentId(null)
                        .build());
            }
        } else {
            boolean exists = likeRepository.existsByUserIdAndCommentId(userId, request.getCommentId());
            if (!exists) {
                likeRepository.save(Like.builder()
                        .userId(userId)
                        .commentId(request.getCommentId())
                        .boardId(null)
                        .build());
            }
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
            likeRepository.deleteByUserIdAndBoardId(userId, request.getBoardId());
        } else {
            likeRepository.deleteByUserIdAndCommentId(userId, request.getCommentId());
        }
    }


}
