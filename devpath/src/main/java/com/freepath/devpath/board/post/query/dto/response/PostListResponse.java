package com.freepath.devpath.board.post.query.dto.response;

import com.freepath.devpath.common.dto.Pagination;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PostListResponse {
    private final List<PostDto> posts;
    private final Pagination pagination;
}
