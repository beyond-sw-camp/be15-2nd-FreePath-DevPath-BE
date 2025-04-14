package com.freepath.devpath.report.dto.response;

import com.freepath.devpath.board.comment.query.dto.CommentDetailDto;
import com.freepath.devpath.board.post.query.dto.response.PostDetailDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class ReportCheckDto {
    private final ReportCheckWithIdDto reportCheckDto;
    private final PostDetailDto postDetailDto;
    private final CommentDetailDto commentDetailDto;
}
