package com.freepath.devpath.csquiz.command.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class CsQuizCreateRequest {
    @NotBlank
    private final String csquizContents;
    @NotNull
    private final int csquizAnswer;
    @NotBlank
    private final String csquizExplanation;
    @NotBlank
    private final String isCsquizSubmitted;

    @NotNull
    private final List<CsQuizOptionCreateRequest> options;
}
