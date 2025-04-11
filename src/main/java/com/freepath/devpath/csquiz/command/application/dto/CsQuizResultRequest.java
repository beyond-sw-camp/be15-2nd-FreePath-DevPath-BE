package com.freepath.devpath.csquiz.command.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CsQuizResultRequest {
    @NotNull
    private final int csquizId;

    @NotNull
    private final int userAnswer;
}
