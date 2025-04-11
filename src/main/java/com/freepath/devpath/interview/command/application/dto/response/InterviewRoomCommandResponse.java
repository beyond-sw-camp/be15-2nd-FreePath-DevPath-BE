package com.freepath.devpath.interview.command.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InterviewRoomCommandResponse {
    private Long interviewRoomId;
    private String interviewRoomTitle;
    private String firstQuestion;
}