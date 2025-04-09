package com.freepath.devpath.csquiz.query.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CsQuizSearchRequest {
    private Integer page = 1;
    private Integer size = 5;
    private int csquizId;
    private String csquizContents;
    private int csquizAnswer;
    private String csquizExplanation;
    private List<CsQuizOptionDTO> options;

    public int getOffset() {
        return (page - 1) * size * 4;
    }

    public int getLimit() {
        return size * 4;
    }
}
