package com.freepath.devpath.board.comment.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class CommentResponseDto {
    private int commentId;
    private int userId;
    private String contents;
    private Date createdAt;
    private Date modifiedAt;
    private boolean isDeleted;
    private Integer parentCommentId;
}
