package com.freepath.devpath.email.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewsSearchRequest {
    private Integer page = 1;
    private Integer size = 10;
    private int itNewsId;
    private String title;
    private String mailingDate;

    public int getOffset() {
        return (page - 1) * size;
    }

    public int getLimit() {
        return size;
    }
}