package com.freepath.devpath.chatting.command.domain.repository;

import com.freepath.devpath.chatting.command.domain.aggregate.Chatting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingRepository extends JpaRepository<Chatting,Integer> {
    Chatting save(Chatting chatting);
}
