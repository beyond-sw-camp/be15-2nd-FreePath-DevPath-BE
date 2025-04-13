package com.freepath.devpath.board.interaction.command.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "댓글 좋아요 요청")
public class CommentLikeRequest extends LikeRequest {
    @Schema(description = "댓글 ID")
    private Long commentId;
}
