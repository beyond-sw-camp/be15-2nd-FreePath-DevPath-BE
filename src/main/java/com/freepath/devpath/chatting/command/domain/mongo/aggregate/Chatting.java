package com.freepath.devpath.chatting.command.domain.mongo.aggregate;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;


import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
@Document(collection="chatting")
public class Chatting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //private int chattingId;
    //@Enumerated(value = EnumType.STRING)
    //private MessageType type;
    private int chattingRoomId;
    private int userId;
    private String chattingMessage;
    private LocalDateTime chattingCreatedAt;
}
