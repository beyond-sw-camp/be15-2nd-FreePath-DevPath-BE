package com.freepath.devpath.csquiz.query.mapper;

import com.freepath.devpath.csquiz.query.dto.CsQuizDetailResultDTO;
import com.freepath.devpath.csquiz.query.dto.CsQuizPreviewDTO;
import com.freepath.devpath.csquiz.query.dto.CsQuizResponse;
import com.freepath.devpath.csquiz.query.dto.CsQuizSearchRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CsQuizQueryMapper {
    List<CsQuizDetailResultDTO> selectAllQuizzes(CsQuizSearchRequest csQuizSearchRequest);

    long countProducts(CsQuizSearchRequest csQuizSearchRequest);


    List<CsQuizPreviewDTO> findWeeklyQuiz();
    CsQuizResponse findQuizById(Long csquizId);
    int countCorrectAnswersByUserId(int userId);
    List<CsQuizDetailResultDTO> findQuizResultsByUserId(int userId);
}

