package com.freepath.devpath.board.vote.command.repository;

import com.freepath.devpath.board.vote.command.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Integer> {

}
