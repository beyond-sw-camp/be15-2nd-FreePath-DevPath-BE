package com.freepath.devpath.board.interaction.command.application.dto;

import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "게시글 북마크 요청", example = "{ \"boardId\": 1 }")
public class BookmarkRequest {

    @Schema(description = "게시글 ID", example = "1")
    @NotNull(message = "게시글 ID는 필수입니다.")
    private Long boardId; 
}
