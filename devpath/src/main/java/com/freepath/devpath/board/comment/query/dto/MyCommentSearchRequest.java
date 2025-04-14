package com.freepath.devpath.board.comment.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyCommentSearchRequest {
    private Integer page = 1;
    private Integer size = 10;
    private Integer userId;
    private String isCommentDeleted;

    public int getOffset() {
        return (page - 1) * size;
    }

    public int getLimit() {
        return size;
    }
}
