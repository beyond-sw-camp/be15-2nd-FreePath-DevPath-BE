package com.freepath.devpath.csquiz.common.repository;

import com.freepath.devpath.csquiz.common.entity.CsQuizResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CsQuizResultRepository extends JpaRepository<CsQuizResult, Integer> {
}
