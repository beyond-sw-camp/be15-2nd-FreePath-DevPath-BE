package com.freepath.devpath.interview.command.application.dto.request;

import lombok.Getter;

@Getter
public class InterviewRoomUpdateCommandRequest {
    private String interviewRoomTitle;
    private String interviewRoomMemo;
}