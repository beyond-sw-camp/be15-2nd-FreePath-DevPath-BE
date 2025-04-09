package com.freepath.devpath.csquiz.query.service;

import com.freepath.devpath.csquiz.query.dto.response.CsQuizDetailResultDTO;
import com.freepath.devpath.csquiz.query.dto.response.CsQuizPreviewDTO;
import com.freepath.devpath.csquiz.query.dto.response.CsQuizResponse;
import com.freepath.devpath.csquiz.query.mapper.CsQuizQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CsQuizQueryService {
    private final CsQuizQueryMapper csQuizQueryMapper;

    public List<CsQuizPreviewDTO> getWeeklyQuiz() {
        return csQuizQueryMapper.findWeeklyQuiz();
    }
    public CsQuizDetailResultDTO getQuizById(Long csquizId) {
        return csQuizQueryMapper.findQuizById(csquizId);
    }
    public List<CsQuizDetailResultDTO> getAllQuizzes() {
        return csQuizQueryMapper.findAllQuizzes();
    }
    public int getCorrectAnswerCount(int userId) {
        return csQuizQueryMapper.countCorrectAnswersByUserId(userId);
    }
    public List<CsQuizDetailResultDTO> getResultsByUserId(int userId) {
        return csQuizQueryMapper.findQuizResultsByUserId(userId);
    }

}

