package com.freepath.devpath.interview.query.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class InterviewRoomDetailResponse {
    private Long interviewRoomId;
    private String interviewRoomTitle;
    private String interviewCategory;
    private LocalDateTime interviewRomCreatedAt;
    private List<InterviewDetailDto> interviewList;
}
