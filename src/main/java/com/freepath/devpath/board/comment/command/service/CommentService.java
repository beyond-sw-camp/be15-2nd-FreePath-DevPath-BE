package com.freepath.devpath.board.comment.command.service;

import com.freepath.devpath.board.comment.command.domain.Comment;
import com.freepath.devpath.board.comment.command.dto.CommentRequestDto;
import com.freepath.devpath.board.comment.command.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    public Comment saveComment(CommentRequestDto dto, int userId) {
        Integer boardId = dto.getBoardId();
        Integer parentCommentId = dto.getParentCommentId();

        // 댓글과 대댓글을 동시에 지정하면 오류
        if ((boardId == null && parentCommentId == null) || (boardId != null && parentCommentId != null)) {
            throw new IllegalArgumentException("댓글 또는 대댓글 중 하나만 지정해야 합니다.");
        }

        if (parentCommentId != null) {
            // 대댓글인 경우, 부모 댓글 가져오기
            Comment parentComment = commentRepository.findById(parentCommentId)
                    .orElseThrow(() -> new IllegalArgumentException("부모 댓글을 찾을 수 없습니다."));

            // 부모 댓글도 대댓글인 경우 → 대대댓글 → 허용 X
            if (parentComment.getParentCommentId() != null) {
                throw new IllegalArgumentException("대댓글의 대댓글은 허용되지 않습니다.");
            }

            // 부모 댓글의 boardId로 설정
            boardId = parentComment.getBoardId();
        }

        Comment comment = Comment.builder()
                .userId(userId)
                .boardId(boardId)
                .parentCommentId(parentCommentId)
                .contents(dto.getContents())
                .createdAt(new Date())
                .Deleted("N")
                .build();

        return commentRepository.save(comment);
    }

    @Transactional
    public void updateComment(int commentId, String newContent, int userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        if (comment.getUserId() != userId) {
            throw new AccessDeniedException("수정 권한이 없습니다.");
        }

        comment.updateContent(newContent);
        commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(int commentId, int userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        if (comment.getUserId() != userId) {
            throw new IllegalStateException("본인이 작성한 댓글만 삭제할 수 있습니다.");
        }

        comment.softDelete();
    }

}
