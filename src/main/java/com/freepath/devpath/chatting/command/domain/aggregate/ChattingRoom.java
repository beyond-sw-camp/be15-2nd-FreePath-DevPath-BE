package com.freepath.devpath.chatting.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="chatting_room")
public class ChattingRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int chattingRoomId;
    private Integer boardId;
    private String chattingRoomTitle;
    private int userCount;
}
