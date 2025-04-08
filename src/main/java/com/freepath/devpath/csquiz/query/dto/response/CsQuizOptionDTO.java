package com.freepath.devpath.csquiz.query.dto.response;

import lombok.Getter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CsQuizOptionDTO{
    private final int optionNo;
    private final String optionContents;
}