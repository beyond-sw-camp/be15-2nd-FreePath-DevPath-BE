package com.freepath.devpath.interview.command.application.controller;

import com.freepath.devpath.board.post.command.exception.FileUpdateFailedException;
import com.freepath.devpath.common.dto.ApiResponse;
import com.freepath.devpath.common.exception.ErrorCode;
import com.freepath.devpath.interview.command.application.dto.request.InterviewAnswerCommandRequest;
import com.freepath.devpath.interview.command.application.dto.request.InterviewRoomCommandRequest;
import com.freepath.devpath.interview.command.application.dto.response.InterviewAnswerCommandResponse;
import com.freepath.devpath.interview.command.application.dto.response.InterviewRoomCommandResponse;
import com.freepath.devpath.interview.command.application.service.InterviewCommandService;
import com.freepath.devpath.interview.command.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/interview-room")
@RequiredArgsConstructor
public class InterviewCommandController {

    private final InterviewCommandService interviewCommandService;

    /* 카테고리 선택으로 면접방 생성하고 첫 질문 도출*/
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    public ResponseEntity<ApiResponse<InterviewRoomCommandResponse>> createRoomAndFirstQuestion(
            @RequestBody InterviewRoomCommandRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = Long.valueOf(userDetails.getUsername());

        InterviewRoomCommandResponse response = interviewCommandService.createRoomAndFirstQuestion(userId, request.getInterviewCategory());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /* 질문에 대한 답변, 답변에 대한 평가, 다음 질문 도출 */
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/{roomId}/interview")
    public ResponseEntity<ApiResponse<InterviewAnswerCommandResponse>> answerAndEvaluate(
            @PathVariable Long roomId,
            @RequestBody InterviewAnswerCommandRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long userId = Long.valueOf(userDetails.getUsername());
        InterviewAnswerCommandResponse response = interviewCommandService.answerAndEvaluate(userId, roomId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // ===== 컨트롤러 레벨 예외 핸들러들 ===== //

    @ExceptionHandler(InterviewRoomCreationException.class)
    public ResponseEntity<ApiResponse<Void>> handleInterviewRoomCreationException(InterviewRoomCreationException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(InterviewQuestionCreationException.class)
    public ResponseEntity<ApiResponse<Void>> handleInterviewQuestionCreationException(InterviewQuestionCreationException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(InterviewRoomNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleInterviewRoomNotFoundException(InterviewRoomNotFoundException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(InterviewRoomAccessException.class)
    public ResponseEntity<ApiResponse<Void>> handleInterviewRoomAccessException(InterviewRoomAccessException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(InterviewIndexInvalidException.class)
    public ResponseEntity<ApiResponse<Void>> handleInterviewIndexInvalidException(InterviewIndexInvalidException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(InterviewAnswerEmptyException.class)
    public ResponseEntity<ApiResponse<Void>> handleInterviewAnswerEmptyException(InterviewAnswerEmptyException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(InterviewEvaluationCreationException.class)
    public ResponseEntity<ApiResponse<Void>> handleInterviewEvaluationCreationException(InterviewEvaluationCreationException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(InterviewSummarizeCreationException.class)
    public ResponseEntity<ApiResponse<Void>> handleInterviewSummarizeCreationException(InterviewSummarizeCreationException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }

}