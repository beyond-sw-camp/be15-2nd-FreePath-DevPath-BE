package com.freepath.devpath.interview.command.domain.repository;

import com.freepath.devpath.interview.command.domain.aggregate.Interview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
    Optional<Interview> findTopByInterviewRoomIdAndInterviewRoleOrderByMessageCreatedAtDesc(
            Long interviewRoomId, Interview.InterviewRole interviewRole
    );
}
