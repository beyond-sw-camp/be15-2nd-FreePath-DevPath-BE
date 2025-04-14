package com.freepath.devpath.board.vote.command.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vote_history")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int voteHistoryId;

    private int voteItemId;

    private int userId;
}
