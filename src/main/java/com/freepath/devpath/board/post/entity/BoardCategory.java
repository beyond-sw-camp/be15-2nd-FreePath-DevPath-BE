package com.freepath.devpath.board.post.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "board_category")
@Getter
@NoArgsConstructor
public class BoardCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int boardCategoryId;

    private int parentBoardCategoryId;

    private String boardCategoryName;
}