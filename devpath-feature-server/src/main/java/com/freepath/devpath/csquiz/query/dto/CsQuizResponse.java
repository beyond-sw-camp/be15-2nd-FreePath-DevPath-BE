package com.freepath.devpath.csquiz.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CsQuizResponse {
    private int csquizId;
    private String csquizContents;
    private int csquizAnswer;
    private String csquizExplanation;
    private List<CsQuizOptionDTO> options;


}
