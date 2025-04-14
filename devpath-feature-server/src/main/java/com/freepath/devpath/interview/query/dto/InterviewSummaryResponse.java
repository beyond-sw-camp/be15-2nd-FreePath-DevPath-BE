package com.freepath.devpath.interview.query.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InterviewSummaryResponse {
    private Long interviewRoomId;
    private String interviewRoomTitle;
    private String summary;
}
