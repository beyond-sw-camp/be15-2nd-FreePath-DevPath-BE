package com.freepath.devpath.chatting.command.domain.mongo.aggregate;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
@Document(collection="chatting")
public class Chatting {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //private int chattingId;
    //@Enumerated(value = EnumType.STRING)
    //private MessageType type;
    @Field("chatting_room_id")
    private int chattingRoomId;
    @Field("user_id")
    private int userId;
    @Field("chatting_message")
    private String chattingMessage;
    @Field("chatting_created_at")
    private LocalDateTime chattingCreatedAt;
}
