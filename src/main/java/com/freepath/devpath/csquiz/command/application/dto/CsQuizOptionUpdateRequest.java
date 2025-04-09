package com.freepath.devpath.csquiz.command.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CsQuizOptionUpdateRequest {

    @NotNull
    private final int optionId;

    @NotNull
    private final int optionNo;

    @NotBlank
    private final String optionContents;
}
