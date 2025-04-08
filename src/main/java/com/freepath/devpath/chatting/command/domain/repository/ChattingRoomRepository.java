package com.freepath.devpath.chatting.command.domain.repository;

import com.freepath.devpath.chatting.command.domain.aggregate.ChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Integer> {

}
