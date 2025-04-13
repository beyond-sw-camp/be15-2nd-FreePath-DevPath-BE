package com.freepath.devpath.csquiz.command.application.controller;

import com.freepath.devpath.common.dto.ApiResponse;
import com.freepath.devpath.common.exception.ErrorCode;
import com.freepath.devpath.csquiz.command.application.dto.CsQuizCreateRequest;
import com.freepath.devpath.csquiz.command.application.dto.CsQuizUpdateRequest;
import com.freepath.devpath.csquiz.command.application.service.CsQuizSchedulerService;
import com.freepath.devpath.csquiz.command.application.service.CsQuizService;
import com.freepath.devpath.csquiz.exception.CsQuizNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/csquiz")
@RequiredArgsConstructor
@Tag(name = "관리자 CS 퀴즈 관리", description = "관리자용 CS 퀴즈 등록, 수정, 제출 관련 API")
public class AdminCsQuizController {

    private final CsQuizService csQuizService;
    private final CsQuizSchedulerService csQuizSchedulerService;

    @Operation(summary = "CS 퀴즈 등록", description = "관리자가 새로운 CS 퀴즈를 등록합니다.")
    @PostMapping("/quiz-add")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody @Valid CsQuizCreateRequest request) {
        csQuizService.registerQuiz(request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "CS 퀴즈 수정", description = "기존 CS 퀴즈 내용을 수정합니다.")
    @PutMapping("/modify/{csquizId}")
    public ResponseEntity<ApiResponse<Void>> update(
            @PathVariable int csquizId,
            @RequestBody @Valid CsQuizUpdateRequest csQuizUpdateRequest
    ) {
        csQuizService.updateQuiz(csquizId, csQuizUpdateRequest);
        return ResponseEntity.ok(ApiResponse.success(null));
    }


    @Operation(summary = "주간 퀴즈 제출 트리거(테스트용)", description = "매주 자동으로 제출되는 주간 퀴즈를 수동으로 트리거합니다.(테스트용)")
    @PostMapping("/submit-weekly")
    public ResponseEntity<ApiResponse<Void>> submitWeeklyQuiz() {
        csQuizSchedulerService.submitWeeklyQuiz();
        return ResponseEntity.ok(ApiResponse.success(null));
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
