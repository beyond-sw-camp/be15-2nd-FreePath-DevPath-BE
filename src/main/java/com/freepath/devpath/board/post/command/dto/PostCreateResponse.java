package com.freepath.devpath.board.post.command.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostCreateResponse {
    private int postId;
}
