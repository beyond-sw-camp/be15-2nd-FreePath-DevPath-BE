package com.freepath.devpath.csquiz.common.repository;

import com.freepath.devpath.csquiz.common.entity.CsQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CsQuizRepository extends JpaRepository<CsQuiz, Integer> {

}
