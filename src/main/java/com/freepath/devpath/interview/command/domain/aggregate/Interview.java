package com.freepath.devpath.interview.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interviewId;

    private Long interviewRoomId;

    @Enumerated(EnumType.STRING)
    private InterviewRole interviewRole;  // USER or AI

    @Lob
    private String interviewMessage;

    private LocalDateTime messageCreatedAt;

    @PrePersist
    protected void onCreate() {
        this.messageCreatedAt = LocalDateTime.now();
    }

    public enum InterviewRole {
        USER, AI
    }
}