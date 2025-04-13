package com.freepath.devpath.csquiz.command.application.controller;

import com.freepath.devpath.common.dto.ApiResponse;
import com.freepath.devpath.common.exception.ErrorCode;
import com.freepath.devpath.csquiz.command.application.dto.CsQuizResultRequest;
import com.freepath.devpath.csquiz.command.application.service.CsQuizResultService;
import com.freepath.devpath.csquiz.exception.CsQuizNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/csquiz")
@RequiredArgsConstructor
@Tag(name = "CS 퀴즈 응답", description = "CS 퀴즈 사용자 응답 제출 관련 API")
public class UserCsQuizController {

    private final CsQuizResultService resultService;

    @Operation(summary = "CS 퀴즈 응답 제출", description = "사용자가 퀴즈에 응답한 내용을 제출합니다. (여러 문항 일괄 제출)")
    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<Void>> submitAnswers(@RequestBody @Valid List<CsQuizResultRequest> requests) {
        resultService.submitAnswers(requests);
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
