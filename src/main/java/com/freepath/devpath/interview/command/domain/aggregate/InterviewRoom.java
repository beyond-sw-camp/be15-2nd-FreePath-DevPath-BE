package com.freepath.devpath.interview.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class InterviewRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interviewRoomId;

    private Long userId;

    private String interviewCategory;

    @Column(nullable = false)
    private String interviewRoomTitle;

    private LocalDateTime interviewRoomCreatedAt;

    @PrePersist
    protected void onCreate() {
        this.interviewRoomCreatedAt = LocalDateTime.now();
    }
}