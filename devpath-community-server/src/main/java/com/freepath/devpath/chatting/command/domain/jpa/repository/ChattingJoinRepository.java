package com.freepath.devpath.chatting.command.domain.jpa.repository;

import com.freepath.devpath.chatting.command.domain.jpa.aggregate.ChattingJoin;
import com.freepath.devpath.chatting.command.domain.jpa.aggregate.ChattingJoinId;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChattingJoinRepository extends JpaRepository<ChattingJoin, ChattingJoinId> {
    @Modifying
    @Query("DELETE FROM ChattingJoin cj WHERE cj.id.chattingRoomId = :chattingRoomId")
    void deleteByChattingRoomId(@Param("chattingRoomId") int chattingRoomId);

    Optional<ChattingJoin> findByIdAndChattingJoinStatus(ChattingJoinId chattingJoinId, char chattingJoinStatus);
}
