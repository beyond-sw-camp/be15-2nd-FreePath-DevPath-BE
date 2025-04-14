package com.freepath.devpath.csquiz.query.dto;

import com.freepath.devpath.common.dto.Pagination;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CsQuizListResponse {
    private final List<CsQuizDetailResultDTO> csQuizList;
    private final Pagination pagination;

}
