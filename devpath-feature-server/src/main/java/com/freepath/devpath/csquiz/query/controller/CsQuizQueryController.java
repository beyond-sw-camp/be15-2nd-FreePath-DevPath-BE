package com.freepath.devpath.csquiz.query.controller;

import com.freepath.devpath.common.dto.ApiResponse;
import com.freepath.devpath.common.exception.ErrorCode;
import com.freepath.devpath.csquiz.exception.CsQuizNotFoundException;
import com.freepath.devpath.csquiz.query.dto.*;
import com.freepath.devpath.csquiz.query.service.CsQuizQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "CS 퀴즈 조회", description = "CS 퀴즈 조회 기능 API")
public class CsQuizQueryController {

    private final CsQuizQueryService csQuizQueryService;

    @Operation(summary = "주간 CS 퀴즈 조회", description = "사용자가 응시할 수 있는 주간 CS 퀴즈 목록을 조회합니다.")
    @GetMapping("/csquiz/weekly")
    public ResponseEntity<ApiResponse<List<CsQuizPreviewDTO>>> getWeeklyQuiz() {
        List<CsQuizPreviewDTO> quizzes = csQuizQueryService.getWeeklyQuiz();
        return ResponseEntity.ok(ApiResponse.success(quizzes));
    }

    @Operation(summary = "CS 퀴즈 단건 조회", description = "관리자가 특정 ID의 CS 퀴즈를 조회합니다.")
    @GetMapping("/admin/csquiz/{csquizId}")
    public CsQuizResponse getQuizById(@PathVariable Long csquizId) {
        return csQuizQueryService.getQuizById(csquizId);
    }

    @Operation(summary = "전체 CS 퀴즈 목록 조회", description = "관리자가 조건에 맞는 전체 CS 퀴즈 목록을 조회합니다.")
    @GetMapping("/admin/csquiz/all")
    public ResponseEntity<ApiResponse<CsQuizListResponse>> getAllQuizzes(CsQuizSearchRequest csQuizSearchRequest) {
        CsQuizListResponse quizzes = csQuizQueryService.getAllQuizzes(csQuizSearchRequest);
        return ResponseEntity.ok(ApiResponse.success(quizzes));
    }

    @Operation(summary = "사용자 CS 퀴즈 정답 개수 조회", description = "사용자의 이번 주 CS퀴즈 정답 개수를 조회합니다.")
    @GetMapping("/csquiz/result/correct-count")
    public int getCorrectAnswerCount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userId = Integer.parseInt(authentication.getName());
        return csQuizQueryService.getCorrectAnswerCount(userId);
    }

    @Operation(summary = "사용자 CS 퀴즈 결과 조회", description = "사용자의 이번 주 CS 퀴즈 정답/오답 상세 결과를 조회합니다.")
    @GetMapping("/csquiz/result")
    public List<CsQuizDetailResultDTO> getCorrectResults() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userId = Integer.parseInt(authentication.getName());
        return csQuizQueryService.getResultsByUserId(userId);
    }

    // ===== 컨트롤러 레벨 예외 핸들러들 ===== //

    @ExceptionHandler(CsQuizNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleCsQuizNotFound(CsQuizNotFoundException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }
}

