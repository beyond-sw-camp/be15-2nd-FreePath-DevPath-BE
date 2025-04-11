package com.freepath.devpath.csquiz.command.application.controller;

import com.freepath.devpath.common.dto.ApiResponse;
import com.freepath.devpath.common.exception.ErrorCode;
import com.freepath.devpath.csquiz.command.application.dto.CsQuizResultRequest;
import com.freepath.devpath.csquiz.command.application.service.CsQuizResultService;
import com.freepath.devpath.csquiz.exception.CsQuizNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/csquiz")
@RequiredArgsConstructor
public class UserCsQuizController {

    private final CsQuizResultService resultService;

    // 사용자 답변 저장 (입력 받은 만큼씩)
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
