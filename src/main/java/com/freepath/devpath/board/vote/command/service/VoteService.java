package com.freepath.devpath.board.vote.command.service;

import com.freepath.devpath.board.vote.command.dto.request.VoteCreateRequest;
import com.freepath.devpath.board.vote.command.entity.Vote;
import com.freepath.devpath.board.vote.command.entity.VoteItem;
import com.freepath.devpath.board.vote.command.repository.VoteItemRepository;
import com.freepath.devpath.board.vote.command.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final VoteItemRepository voteItemRepository;

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
}