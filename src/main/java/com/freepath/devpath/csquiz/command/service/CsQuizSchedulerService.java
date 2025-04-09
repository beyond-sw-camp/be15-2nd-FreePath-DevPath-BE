package com.freepath.devpath.csquiz.command.service;

import com.freepath.devpath.csquiz.command.entity.CsQuiz;
import com.freepath.devpath.csquiz.command.repository.CsQuizRepository;
import com.freepath.devpath.csquiz.command.repository.CsQuizResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CsQuizSchedulerService {

    private final CsQuizRepository csQuizRepository;

    private final CsQuizResultRepository csQuizResultRepository;

    @Transactional
    public void submitWeeklyQuiz() {
        csQuizResultRepository.deleteAll(); // 퀴즈 결과 테이블에 있는 데이터 삭제

        List<CsQuiz> unsubmittedQuizzes = csQuizRepository
                .findTop10ByIsCsquizSubmittedOrderByCsquizIdAsc("N");

        for (CsQuiz quiz : unsubmittedQuizzes) {
            quiz.setIsCsquizSubmitted("Y");
        }
    }
}
