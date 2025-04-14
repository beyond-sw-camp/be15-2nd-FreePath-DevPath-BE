package com.freepath.devpath.board.vote.command.repository;

import com.freepath.devpath.board.vote.command.entity.VoteHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteHistoryRepository extends JpaRepository<VoteHistory, Integer> {
    Boolean existsByUserIdAndVoteItemId(int userId, int voteItemId);
}
