package com.freepath.devpath.board.post.board.domain;

import jakarta.persistence.*;

@Entity
public class BoardCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_category_id")
    private int boardCategoryId;
}
