package com.freepath.devpath.board.comment.command.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRequestDto {


    private Integer boardId;
    private Integer parentCommentId;
    private String contents;

    @Builder
    public CommentRequestDto(Integer boardId, Integer parentCommentId, String contents) {
        this.boardId = boardId;
        this.parentCommentId = parentCommentId;
        this.contents = contents;
    }
}
