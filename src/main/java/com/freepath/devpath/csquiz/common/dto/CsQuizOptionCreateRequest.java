package com.freepath.devpath.csquiz.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CsQuizOptionCreateRequest {
    @NotNull
    private final int optionNo;

    @NotBlank
    private final String optionContents;
}
