package com.freepath.devpath.chattingroom.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="chatting_join")
public class ChattingJoin {

    @EmbeddedId
    private ChattingJoinId id;

    @Enumerated(EnumType.STRING)
    private ChattingRole chattingRole;

    private char chattingJoinStatus;
}
