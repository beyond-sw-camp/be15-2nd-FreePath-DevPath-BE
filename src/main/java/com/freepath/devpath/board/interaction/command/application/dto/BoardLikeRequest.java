package com.freepath.devpath.board.interaction.command.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "게시글 좋아요 요청")
public class BoardLikeRequest extends LikeRequest {
    @Schema(description = "게시글 ID")
    private Long boardId;
}
