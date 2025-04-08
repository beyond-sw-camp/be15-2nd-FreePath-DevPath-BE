package com.freepath.devpath.chattingroom.command.domain.repository;

import com.freepath.devpath.chattingroom.command.domain.aggregate.ChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Integer> {

}
