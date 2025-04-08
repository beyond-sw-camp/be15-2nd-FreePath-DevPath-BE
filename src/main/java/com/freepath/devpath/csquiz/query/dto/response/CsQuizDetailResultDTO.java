package com.freepath.devpath.csquiz.query.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CsQuizDetailResultDTO {
    private int csquizId;
    private String csquizContents;
    private int userAnswer;
    private String isCsquizCorrect;
    private int csquizAnswer;
    private String csquizExplanation;
    private List<CsQuizOptionDTO> options;
}
