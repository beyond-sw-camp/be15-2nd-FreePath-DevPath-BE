package com.freepath.devpath.csquiz.query.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CsQuizPreviewDTO {
    private int csquizId;
    private String csquizContents;
    private List<CsQuizOptionDTO> options;
}
