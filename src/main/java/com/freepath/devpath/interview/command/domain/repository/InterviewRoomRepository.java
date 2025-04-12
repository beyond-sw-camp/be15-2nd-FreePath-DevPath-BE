package com.freepath.devpath.interview.command.domain.repository;

import com.freepath.devpath.interview.command.domain.aggregate.InterviewRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface InterviewRoomRepository extends JpaRepository<InterviewRoom, Long> {
    /* 상태를 expire 시킬 면접방 조회 */
    List<InterviewRoom> findAllByInterviewRoomStatus(InterviewRoom.InterviewRoomStatus status);


}