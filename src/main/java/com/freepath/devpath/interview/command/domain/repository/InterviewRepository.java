package com.freepath.devpath.interview.command.domain.repository;

import com.freepath.devpath.interview.command.domain.aggregate.Interview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
}
