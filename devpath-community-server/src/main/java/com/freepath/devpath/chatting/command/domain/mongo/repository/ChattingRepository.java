package com.freepath.devpath.chatting.command.domain.mongo.repository;

import com.freepath.devpath.chatting.command.domain.mongo.aggregate.Chatting;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface ChattingRepository extends MongoRepository<Chatting,String> {
    Chatting save(Chatting chatting);

    void deleteByChattingRoomId(int chattingRoomId);

    List<Chatting> findByChattingRoomId(int chattingRoomId);
}
