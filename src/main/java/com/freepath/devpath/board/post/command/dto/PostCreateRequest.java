package com.freepath.devpath.board.post.command.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostCreateRequest {
    private Integer boardCategory;     // 게시글 카테고리 ID
    private String boardTitle;         // 제목
    private String boardContents;      // 내용
}
