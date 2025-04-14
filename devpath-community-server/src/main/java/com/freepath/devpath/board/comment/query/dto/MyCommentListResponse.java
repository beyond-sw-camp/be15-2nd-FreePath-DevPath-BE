package com.freepath.devpath.board.comment.query.dto;

import com.freepath.devpath.common.dto.Pagination;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MyCommentListResponse {
    private List<MyCommentResponseDto> comments;
    private Pagination pagination;
}
