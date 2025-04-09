package com.freepath.devpath.csquiz.query.mapper;

import com.freepath.devpath.csquiz.query.dto.response.CsQuizDetailResultDTO;
import com.freepath.devpath.csquiz.query.dto.response.CsQuizPreviewDTO;
import com.freepath.devpath.csquiz.query.dto.response.CsQuizResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CsQuizQueryMapper {
    List<CsQuizPreviewDTO> findWeeklyQuiz();
    CsQuizDetailResultDTO findQuizById(Long csquizId);
    List<CsQuizDetailResultDTO> findAllQuizzes();
    int countCorrectAnswersByUserId(int userId);
    List<CsQuizDetailResultDTO> findQuizResultsByUserId(int userId);
}

