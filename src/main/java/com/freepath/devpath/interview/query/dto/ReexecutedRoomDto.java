package com.freepath.devpath.interview.query.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReexecutedRoomDto {
    private Long interviewRoomId;
    private String interviewRoomTitle;
    private LocalDateTime interviewRoomCreatedAt;
}
