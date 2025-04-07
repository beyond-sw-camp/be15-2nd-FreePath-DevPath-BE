package com.freepath.devpath.chattingroom.command.domain.aggregate;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class ChattingJoinId implements Serializable {

    @Column(name = "chatting_room_id")
    private Integer chattingRoomId;

    @Column(name = "user_id")
    private Integer userId;

    // equals & hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChattingJoinId)) return false;
        ChattingJoinId that = (ChattingJoinId) o;
        return Objects.equals(chattingRoomId, that.chattingRoomId) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chattingRoomId, userId);
    }
}
