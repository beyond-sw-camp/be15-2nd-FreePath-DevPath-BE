package com.freepath.devpath.csquiz.common.service;

import com.freepath.devpath.csquiz.common.dto.CsQuizResultRequest;
import com.freepath.devpath.csquiz.common.entity.CsQuiz;
import com.freepath.devpath.csquiz.common.entity.CsQuizResult;
import com.freepath.devpath.csquiz.common.repository.CsQuizRepository;
import com.freepath.devpath.csquiz.common.repository.CsQuizResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CsQuizResultService {

    private final CsQuizResultRepository resultRepository;
    private final CsQuizRepository quizRepository;

    @Transactional
    public void submitAnswer(CsQuizResultRequest request) {
        CsQuiz quiz = quizRepository.findById(request.getCsquizId())
                .orElseThrow(() -> new IllegalArgumentException("해당 퀴즈가 존재하지 않습니다."));

        String isCorrect = quiz.getCsquizAnswer() == request.getUserAnswer() ? "Y" : "N";

        CsQuizResult result = new CsQuizResult();
        result.setCsquizId(request.getCsquizId());
        result.setUserId(request.getUserId());
        result.setUserAnswer(request.getUserAnswer());
        result.setIsCsquizCorrect(isCorrect);

        resultRepository.save(result);
    }
}
