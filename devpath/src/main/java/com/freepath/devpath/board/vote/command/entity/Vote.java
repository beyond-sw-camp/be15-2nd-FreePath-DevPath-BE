package com.freepath.devpath.board.vote.command.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "vote")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int voteId;

    private int boardId;

    private String voteTitle;

    private LocalDateTime voteDuedate;

    private Character isVoteFinished;

    public void changeVoteDueDate(LocalDateTime localDateTime) {
        this.voteDuedate = localDateTime;
    }

    public void setIsVoteFinished() {
        this.isVoteFinished = 'Y';
    }
}
