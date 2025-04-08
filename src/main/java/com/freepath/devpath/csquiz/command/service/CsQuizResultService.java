package com.freepath.devpath.csquiz.command.service;

import com.freepath.devpath.csquiz.command.dto.request.CsQuizResultRequest;
import com.freepath.devpath.csquiz.command.entity.CsQuiz;
import com.freepath.devpath.csquiz.command.entity.CsQuizResult;
import com.freepath.devpath.csquiz.command.repository.CsQuizRepository;
import com.freepath.devpath.csquiz.command.repository.CsQuizResultRepository;
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
                .orElseThrow(() -> new IllegalArgumentException("해당 퀴즈가 존재하지 않습니다."));

        String isCorrect = quiz.getCsquizAnswer() == request.getUserAnswer() ? "Y" : "N";

        CsQuizResult result = new CsQuizResult();
        result.setCsquizId(request.getCsquizId());
        result.setUserId(userId);
        result.setUserAnswer(request.getUserAnswer());
        result.setIsCsquizCorrect(isCorrect);

        resultRepository.save(result);
    }
}
