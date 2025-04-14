package com.freepath.devpath.report.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ReportCheckListResponse {
    private final List<ReportCheckDto> reportCheckList;
}
