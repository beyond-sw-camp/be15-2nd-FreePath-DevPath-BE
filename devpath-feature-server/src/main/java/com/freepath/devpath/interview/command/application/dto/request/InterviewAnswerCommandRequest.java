package com.freepath.devpath.interview.command.application.dto.request;

import lombok.Getter;

@Getter
public class InterviewAnswerCommandRequest {
    private String userAnswer;
    private int interviewIndex; // 3개의 질답 진행 확인
}