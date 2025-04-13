package com.freepath.devpath.board.interaction.command.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(
        description = "좋아요 요청 - 게시글 또는 댓글 중 하나에 좋아요를 등록합니다.",
        oneOf = {BoardLikeRequest.class, CommentLikeRequest.class}
)
public abstract class LikeRequest {
}
