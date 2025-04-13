package com.freepath.devpath.board.interaction.command.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "게시글 북마크 요청", example = "{ \"boardId\": 1 }")
public class BookmarkRequest {

    @Schema(description = "게시글 ID", example = "1", required = true)
    private Long boardId;
}
