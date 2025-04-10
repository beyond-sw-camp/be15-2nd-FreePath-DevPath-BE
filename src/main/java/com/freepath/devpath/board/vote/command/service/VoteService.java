package com.freepath.devpath.board.vote.command.service;

import com.freepath.devpath.board.vote.command.dto.request.VoteCreateRequest;
import com.freepath.devpath.board.vote.command.dto.request.VoteParticipateRequest;
import com.freepath.devpath.board.vote.command.entity.Vote;
import com.freepath.devpath.board.vote.command.entity.VoteHistory;
import com.freepath.devpath.board.vote.command.entity.VoteItem;
import com.freepath.devpath.board.vote.command.exception.VoteParticipateFailedException;
import com.freepath.devpath.board.vote.command.repository.VoteHistoryRepository;
import com.freepath.devpath.board.vote.command.repository.VoteItemRepository;
import com.freepath.devpath.board.vote.command.repository.VoteRepository;
import com.freepath.devpath.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}