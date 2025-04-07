package com.freepath.devpath.chattingroom.command.domain.repository;

import com.freepath.devpath.chattingroom.command.domain.aggregate.ChattingJoin;
import com.freepath.devpath.chattingroom.command.domain.aggregate.ChattingJoinId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingJoinRepository extends JpaRepository<ChattingJoin, ChattingJoinId> {
}
