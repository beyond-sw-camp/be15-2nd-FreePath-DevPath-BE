package com.freepath.devpath.chatting.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="user_block")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBlock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int blockId;
    private int blockerId;
    private  int blockedId;
}
