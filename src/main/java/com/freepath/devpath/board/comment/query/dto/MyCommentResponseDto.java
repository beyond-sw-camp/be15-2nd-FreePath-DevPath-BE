package com.freepath.devpath.board.comment.query.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class MyCommentResponseDto {

    private String boardTitle;
    private String contents;
    private Date createdAt;
    private Date modifiedAt;
}
