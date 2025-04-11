package com.freepath.devpath.csquiz.query.controller;

import com.freepath.devpath.common.dto.ApiResponse;
import com.freepath.devpath.csquiz.query.dto.*;
import com.freepath.devpath.csquiz.query.service.CsQuizQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CsQuizQueryController {

    private final CsQuizQueryService csQuizQueryService;

    @GetMapping("/csquiz/weekly") // cs 퀴즈 버전 갱신 사용자가 실행할 주간 cs퀴즈 목록 조회
    public ResponseEntity<ApiResponse<List<CsQuizPreviewDTO>>> getWeeklyQuiz() {
        List<CsQuizPreviewDTO> quizzes = csQuizQueryService.getWeeklyQuiz();
        return ResponseEntity.ok(ApiResponse.success(quizzes));
    }

    @GetMapping("/admin/csquiz/{csquizId}") // 관리자의 csquiz 조회
    public CsQuizResponse getQuizById(@PathVariable Long csquizId) {
        return csQuizQueryService.getQuizById(csquizId);
    }

    @GetMapping("/admin/csquiz/all") // 모든 퀴즈 조회
    public ResponseEntity<ApiResponse<CsQuizListResponse>> getAllQuizzes(CsQuizSearchRequest csQuizSearchRequest) {
        CsQuizListResponse quizzes = csQuizQueryService.getAllQuizzes(csQuizSearchRequest);
        return ResponseEntity.ok(ApiResponse.success(quizzes));
    }

    @GetMapping("/csquiz/result/correct-count") // 사용자의 이번주 CS퀴즈 정답 개수 조회
    public int getCorrectAnswerCount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userId = Integer.parseInt(authentication.getName());
        return csQuizQueryService.getCorrectAnswerCount(userId);
    }

    @GetMapping("/csquiz/result")   // 사용자의 이번 주 CS 퀴즈 결과 조회
    public List<CsQuizDetailResultDTO> getCorrectResults() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userId = Integer.parseInt(authentication.getName());
        return csQuizQueryService.getResultsByUserId(userId);
    }


}

