package com.freepath.devpath.board.vote.command.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VoteParticipateRequest {
    @Min(value = 1, message = "투표 항목 ID는 1 이상이어야 합니다.")
    private int voteItemId;
}
