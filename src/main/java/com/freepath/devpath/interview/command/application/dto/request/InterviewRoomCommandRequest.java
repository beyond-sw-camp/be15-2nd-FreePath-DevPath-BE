package com.freepath.devpath.interview.command.application.dto.request;

import com.freepath.devpath.interview.command.domain.aggregate.DifficultyLevel;
import com.freepath.devpath.interview.command.domain.aggregate.EvaluationStrictness;
import lombok.Getter;

@Getter
public class InterviewRoomCommandRequest {
    private Long userId;
    private String interviewRoomTitle;
    private String interviewCategory;
    private DifficultyLevel difficultyLevel;
    private EvaluationStrictness evaluationStrictness;
    private String interviewRoomMemo;
}