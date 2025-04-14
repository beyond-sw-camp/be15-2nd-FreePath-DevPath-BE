package com.freepath.devpath.board.vote.query.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class VoteDetailDTO {
    private int voteId;
    private String voteTitle;
    private Character isVoteFinished;
    private int totalVoteCount;
    private Integer selectedItemId; // 선택된 항목이 없을 수 있으므로 Integer로 처리
    private List<VoteItemDTO> voteItems; // vote_items에 대한 목록
}
