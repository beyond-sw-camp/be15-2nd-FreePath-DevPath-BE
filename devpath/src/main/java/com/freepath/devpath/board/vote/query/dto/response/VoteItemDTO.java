package com.freepath.devpath.board.vote.query.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VoteItemDTO {
    private int voteItemId;
    private String voteItemTitle;
    private int voteCount;
}
