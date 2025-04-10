package com.freepath.devpath.board.vote.query.service;

import com.freepath.devpath.board.vote.query.dto.response.VoteDetailDTO;
import com.freepath.devpath.board.vote.query.dto.response.VoteDetailResponse;
import com.freepath.devpath.board.vote.query.mapper.VoteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoteQueryService {
    private final VoteMapper voteMapper;

    // 게시물 ID와 사용자 ID를 기반으로 투표 상세 정보를 조회
    public VoteDetailResponse getVoteDetail(int userId, int boardId) {
        VoteDetailDTO voteDetailDTO = voteMapper.selectVoteDetail(userId, boardId);
        System.out.println(voteDetailDTO);

        return VoteDetailResponse.builder()
                .voteDetailDTO(voteDetailDTO)
                .build();
    }

}
