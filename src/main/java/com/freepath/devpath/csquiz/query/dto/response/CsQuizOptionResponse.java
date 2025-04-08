package com.freepath.devpath.csquiz.query.dto.response;

import com.freepath.devpath.csquiz.command.entity.CsQuizOption;
import lombok.Getter;

@Getter
public class CsQuizOptionResponse {

    private final int optionNo;
    private final String optionContents;

    public CsQuizOptionResponse(int optionNo, String optionContents) {
        this.optionNo = optionNo;
        this.optionContents = optionContents;
    }

    public static CsQuizOptionResponse fromEntity(CsQuizOption csQuizOption) {
        return new CsQuizOptionResponse(csQuizOption.getOptionNo(), csQuizOption.getOptionContents());
    }
}
