package com.freepath.devpath.csquiz.command.repository;

import com.freepath.devpath.csquiz.command.entity.CsQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CsQuizRepository extends JpaRepository<CsQuiz, Integer> {

    List<CsQuiz> findTop10ByIsCsquizSubmittedOrderByCsquizIdAsc(String n);

    List<CsQuiz> findTop10ByIsCsquizSubmittedOrderByCsquizIdDesc(String y);
}
