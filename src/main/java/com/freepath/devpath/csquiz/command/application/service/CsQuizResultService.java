package com.freepath.devpath.csquiz.command.application.service;

import com.freepath.devpath.common.exception.ErrorCode;
import com.freepath.devpath.csquiz.command.application.dto.CsQuizResultRequest;
import com.freepath.devpath.csquiz.command.domain.aggregate.CsQuiz;
import com.freepath.devpath.csquiz.command.domain.aggregate.CsQuizResult;
import com.freepath.devpath.csquiz.command.domain.repository.CsQuizRepository;
import com.freepath.devpath.csquiz.command.domain.repository.CsQuizResultRepository;
import com.freepath.devpath.csquiz.exception.CsQuizNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
@RequiredArgsConstructor
public class CsQuizResultService {

    private final CsQuizResultRepository resultRepository;
    private final CsQuizRepository quizRepository;

    @Transactional
    public void submitAnswer(CsQuizResultRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userId = Integer.parseInt(authentication.getName());

        CsQuiz quiz = quizRepository.findById(request.getCsquizId())
                .orElseThrow(() -> new CsQuizNotFoundException(ErrorCode.CS_QUIZ_NOT_FOUND));

        String isCorrect = quiz.getCsquizAnswer() == request.getUserAnswer() ? "Y" : "N";

        CsQuizResult result = new CsQuizResult();
        result.setCsquizId(request.getCsquizId());
        result.setUserId(userId);
        result.setUserAnswer(request.getUserAnswer());
        result.setIsCsquizCorrect(isCorrect);

        resultRepository.save(result);
    }
}
