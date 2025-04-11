package com.freepath.devpath.board.interaction.query.dto;

import com.freepath.devpath.board.post.query.dto.response.PostDto;
import com.freepath.devpath.common.dto.Pagination;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MypagePostListResponse {
    private Pagination pagination;
    private List<PostDto> posts;
}
