package com.freepath.devpath.board.vote.query.service;

import com.freepath.devpath.board.post.query.exception.NoSuchPostException;
import com.freepath.devpath.board.post.query.mapper.PostMapper;
import com.freepath.devpath.board.vote.command.repository.VoteRepository;
import com.freepath.devpath.board.vote.query.dto.response.VoteDetailDTO;
import com.freepath.devpath.board.vote.query.dto.response.VoteDetailResponse;
import com.freepath.devpath.board.vote.query.mapper.VoteMapper;
import com.freepath.devpath.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoteQueryService {
    private final VoteMapper voteMapper;
    private final PostMapper postMapper;

    // 게시물 ID와 사용자 ID를 기반으로 투표 상세 정보를 조회
    public VoteDetailResponse getVoteDetail(int userId, int boardId) {

        if (!postMapper.existsById(boardId)) {
            throw new NoSuchPostException(ErrorCode.POST_NOT_FOUND);
        }

        VoteDetailDTO voteDetailDTO = voteMapper.selectVoteDetail(userId, boardId);

        return VoteDetailResponse.builder()
                .voteDetailDTO(voteDetailDTO)
                .build();
    }

}
