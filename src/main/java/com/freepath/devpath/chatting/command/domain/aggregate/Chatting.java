package com.freepath.devpath.chatting.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
@Entity
@Table(name="chatting")
public class Chatting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int chattingId;
    //@Enumerated(value = EnumType.STRING)
    //private MessageType type;
    private int chattingRoomId;
    private int userId;
    private String chattingMessage;
    private LocalDateTime chattingCreatedAt;
}
