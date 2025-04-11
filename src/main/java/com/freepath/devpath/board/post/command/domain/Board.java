package com.freepath.devpath.board.post.command.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "board")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int boardId;

    private int boardCategory;

    private int userId;

    private String boardTitle;

    private String boardContents;

    // insert 시 JPA가 이 컬럼에 값을 명시하지 않음 -> DDL 상의 DEFAULT 값으로 생성하기 위한 조건
    @Column(insertable = false)
    private Date boardCreatedAt;

    private Date boardModifiedAt;

    @Column(insertable = false)
    private String isBoardDeleted;


    public void delete() {
        this.isBoardDeleted = "Y";
    }

    public void modifyTitleAndContent(String modifiedTitle, String modiifiedContents) {
        this.boardTitle = modifiedTitle;
        this.boardContents = modiifiedContents;
    }
}