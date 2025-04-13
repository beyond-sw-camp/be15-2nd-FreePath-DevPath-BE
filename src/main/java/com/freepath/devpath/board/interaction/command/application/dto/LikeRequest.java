package com.freepath.devpath.board.interaction.command.application.dto;

import lombok.*;

@Getter
@NoArgsConstructor
public class LikeRequest {
    private Long boardId;
    private Long commentId;
}