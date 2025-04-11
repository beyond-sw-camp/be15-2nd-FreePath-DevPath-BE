package com.freepath.devpath.interview.command.application.dto.request;

import lombok.Getter;

@Getter
public class InterviewRoomCommandRequest {
    private Long userId;
    private String interviewRoomTitle;
    private String interviewCategory;
}