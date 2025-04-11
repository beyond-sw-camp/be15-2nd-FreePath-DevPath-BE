package com.freepath.devpath.board.vote.query.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VoteDetailResponse {
    private final VoteDetailDTO voteDetailDTO;
}
