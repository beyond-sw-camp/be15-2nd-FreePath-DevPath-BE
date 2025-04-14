package com.freepath.devpath.board.post.query.dto.response;

import com.freepath.devpath.common.dto.Pagination;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MyPostListResponse {
    private final List<PostDto> myPosts;
    private final Pagination pagination;
}
