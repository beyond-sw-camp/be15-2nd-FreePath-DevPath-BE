package com.freepath.devpath.csquiz.command.service;

import com.freepath.devpath.csquiz.command.entity.CsQuiz;
import com.freepath.devpath.csquiz.command.repository.CsQuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CsQuizSchedulerService {

    private final CsQuizRepository csQuizRepository;



    @Transactional
    public void submitWeeklyQuiz() {
        List<CsQuiz> unsubmittedQuizzes = csQuizRepository
                .findTop10ByIsCsquizSubmittedOrderByCsquizIdAsc("N");

        for (CsQuiz quiz : unsubmittedQuizzes) {
            quiz.setIsCsquizSubmitted("Y");
        }
    }
}
