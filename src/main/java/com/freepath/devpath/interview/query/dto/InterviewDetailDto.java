package com.freepath.devpath.interview.query.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class InterviewDetailDto {
    private String interviewRole;
    private String interviewMessage;
    private LocalDateTime messageCreatedAt;
}
