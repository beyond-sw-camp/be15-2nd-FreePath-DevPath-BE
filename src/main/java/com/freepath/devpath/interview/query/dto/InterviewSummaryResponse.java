package com.freepath.devpath.interview.query.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InterviewSummaryResponse {
    private String interviewRoomId;
    private String interviewRoomTitle;
    private String summary;
}
