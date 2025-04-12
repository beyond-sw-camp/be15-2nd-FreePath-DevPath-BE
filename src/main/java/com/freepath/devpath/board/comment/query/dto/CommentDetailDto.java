package com.freepath.devpath.board.comment.query.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class CommentDetailDto {
    private String nickname;
    private String commentContents;
    private Date commentCreatedAt;
    private Date commentModifiedAt;
    private String isCommentDeleted;
}
