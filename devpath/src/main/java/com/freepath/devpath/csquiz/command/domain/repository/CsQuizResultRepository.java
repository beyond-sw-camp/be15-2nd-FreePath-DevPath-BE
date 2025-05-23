package com.freepath.devpath.csquiz.command.domain.repository;

import com.freepath.devpath.csquiz.command.domain.aggregate.CsQuizResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CsQuizResultRepository extends JpaRepository<CsQuizResult, Integer> {

    void deleteAll();
}
