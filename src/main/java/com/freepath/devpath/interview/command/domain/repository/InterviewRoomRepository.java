package com.freepath.devpath.interview.command.domain.repository;

import com.freepath.devpath.interview.command.domain.aggregate.InterviewRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewRoomRepository extends JpaRepository<InterviewRoom, Long> {
}