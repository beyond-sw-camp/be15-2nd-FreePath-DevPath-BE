package com.freepath.devpath.interview.query.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Builder
public class InterviewSummaryResponse {
    private String interviewRoomId;
    private String intervieRoomTitle;
    private String summary;
}
