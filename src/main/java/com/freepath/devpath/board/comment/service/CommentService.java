package com.freepath.devpath.board.comment.service;

import com.freepath.devpath.board.comment.domain.Comment;
import com.freepath.devpath.board.comment.dto.CommentRequestDto;
import com.freepath.devpath.board.comment.repository.CommentRepository;
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

        if ((boardId == null && parentCommentId == null) || (boardId != null && parentCommentId != null)) {
            throw new IllegalArgumentException("댓글 또는 대댓글 중 하나만 지정해야 합니다.");
        }

        if (parentCommentId != null) {
            // 대댓글인 경우, 부모 댓글로부터 boardId 가져오기
            Comment parentComment = commentRepository.findById(parentCommentId)
                    .orElseThrow(() -> new IllegalArgumentException("부모 댓글을 찾을 수 없습니다."));
            boardId = parentComment.getBoardId(); // 부모 댓글의 boardId로 설정
        }

        Comment comment = Comment.builder()
                .userId(userId)
                .boardId(boardId)
                .parentCommentId(parentCommentId)
                .contents(dto.getContents())
                .createdAt(new Date())
                .Deleted("N")
                .build();

        commentRepository.save(comment);
        return comment;
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
