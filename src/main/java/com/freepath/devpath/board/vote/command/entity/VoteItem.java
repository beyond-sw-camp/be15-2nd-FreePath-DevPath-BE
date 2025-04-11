package com.freepath.devpath.board.vote.command.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vote_item")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int voteItemId;

    private int voteId;

    private String voteItemTitle;

    private int voteCount;

    public void increaseVoteCount() {
        this.voteCount++;
    }
}
