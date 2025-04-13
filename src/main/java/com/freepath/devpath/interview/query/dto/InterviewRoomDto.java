package com.freepath.devpath.interview.query.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InterviewRoomDto {
    /* 면접방 목록 조회 시 사용 */
    private Long interviewRoomId;
    private Long userId;
    private String interviewRoomTitle;
    private String interviewCategory;
    private String difficultyLevel;
    private String evaluationStrictness;
    private String interviewRoomStatus;
    private LocalDateTime interviewRoomCreatedAt;
}