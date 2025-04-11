package com.freepath.devpath.board.interaction.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookmarkedBoardSearchRequest {

    private Integer page = 1;
    private Integer size = 5;

    public int getOffset() {
        return (page - 1) * size;
    }

    public int getLimit() {
        return size;
    }
}
