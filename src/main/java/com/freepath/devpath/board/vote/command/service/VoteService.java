package com.freepath.devpath.board.vote.command.service;

import com.freepath.devpath.board.vote.command.dto.request.VoteCreateRequest;
import com.freepath.devpath.board.vote.command.dto.request.VoteParticipateRequest;
import com.freepath.devpath.board.vote.command.entity.Vote;
import com.freepath.devpath.board.vote.command.entity.VoteHistory;
import com.freepath.devpath.board.vote.command.entity.VoteItem;
import com.freepath.devpath.board.vote.command.exception.VoteEndFailedException;
import com.freepath.devpath.board.vote.command.exception.VoteParticipateFailedException;
import com.freepath.devpath.board.vote.command.repository.VoteHistoryRepository;
import com.freepath.devpath.board.vote.command.repository.VoteItemRepository;
import com.freepath.devpath.board.vote.command.repository.VoteRepository;
import com.freepath.devpath.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@RequiredArgsConstructor
@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final VoteItemRepository voteItemRepository;
    private final VoteHistoryRepository voteHistoryRepository;

    @Transactional
    public void createVote(VoteCreateRequest voteRequest, int boardId) {
        Vote vote = Vote.builder()
                .boardId(boardId)
                .voteTitle(voteRequest.getVoteTitle())
                .voteDuedate(voteRequest.getVoteDuedate())
                .isVoteFinished('N')
                .build();

        Vote savedVote = voteRepository.save(vote);

        for (String option : voteRequest.getOptions()) {
            VoteItem item = VoteItem.builder()
                    .voteId(savedVote.getVoteId())
                    .voteItemTitle(option)
                    .build();
            voteItemRepository.save(item);
        }
    }

    @Transactional
    public void participateVote(VoteParticipateRequest voteParticipateRequest, int userId) {
        int voteItemId = voteParticipateRequest.getVoteItemId();

        if (voteHistoryRepository.existsByUserIdAndVoteItemId(userId, voteItemId)) {
            throw new VoteParticipateFailedException(ErrorCode.VOTE_ALREADY_VOTED);
        }

        VoteHistory voteHistory = VoteHistory.builder()
                .userId(userId)
                .voteItemId(voteItemId)
                .build();

        voteHistoryRepository.save(voteHistory);
    }

    @Transactional
    public void endVote(int voteId, int userId) {
        Vote vote = voteRepository.findById(voteId).
                orElseThrow(() -> new RuntimeException("존재하지 않는 투표입니다."));

        if (vote.getIsVoteFinished() == 'Y') {
            throw new VoteEndFailedException(ErrorCode.VOTE_ALREADY_ENDED);
        }

        int ownerId = voteRepository.findUserIdByVoteId(voteId);

        // 게시글 작성자 확인
        if (ownerId != userId) {
            throw new VoteEndFailedException(ErrorCode.VOTE_END_FAILED);
        }

        // 투표 종료 처리
        vote.changeVoteDueDate(LocalDateTime.now());
        vote.setIsVoteFinished();
    }
}