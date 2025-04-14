package com.freepath.devpath.interview.command.domain.repository;

import com.freepath.devpath.interview.command.domain.aggregate.Interview;
import com.freepath.devpath.interview.command.domain.aggregate.InterviewRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
    Optional<Interview> findTopByInterviewRoomIdAndInterviewRoleOrderByInterviewIdDesc(
            Long interviewRoomId, InterviewRole interviewRole
    );

    List<Interview> findByInterviewRoomId(Long roomId);

    List<Interview> findByInterviewRoomIdAndInterviewRoleOrderByInterviewIdAsc(Long interviewRoomId, InterviewRole role);

    void deleteByInterviewRoomId(Long interviewRoomId);
}
