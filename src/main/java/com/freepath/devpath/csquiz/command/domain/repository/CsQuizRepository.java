package com.freepath.devpath.csquiz.command.domain.repository;

import com.freepath.devpath.csquiz.command.domain.aggregate.CsQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CsQuizRepository extends JpaRepository<CsQuiz, Integer> {

    List<CsQuiz> findTop10ByIsCsquizSubmittedOrderByCsquizIdAsc(String n);


}
