package com.freepath.devpath.interview.command.application.controller;

import com.freepath.devpath.common.dto.ApiResponse;
import com.freepath.devpath.common.exception.ErrorCode;
import com.freepath.devpath.interview.command.application.dto.request.InterviewAnswerCommandRequest;
import com.freepath.devpath.interview.command.application.dto.request.InterviewRoomCommandRequest;
import com.freepath.devpath.interview.command.application.dto.request.InterviewRoomUpdateCommandRequest;
import com.freepath.devpath.interview.command.application.dto.response.InterviewAnswerCommandResponse;
import com.freepath.devpath.interview.command.application.dto.response.InterviewRoomCommandResponse;
import com.freepath.devpath.interview.command.application.service.InterviewCommandService;
import com.freepath.devpath.interview.command.domain.aggregate.EvaluationStrictness;
import com.freepath.devpath.interview.command.exception.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "면접방 및 면접 흐름 제어", description = "면접방 생성, 답변, 평가, 수정, 삭제 등 사용자 중심의 면접 관리 기능 제공 API")
@RestController
@RequestMapping("/interview-room")
@RequiredArgsConstructor
public class InterviewCommandController {

    private final InterviewCommandService interviewCommandService;

    /* 카테고리 선택으로 면접방 생성하고 첫 질문 도출*/
    @Operation(summary = "면접방 생성 및 첫 질문 도출", description = "카테고리 및 난이도 정보를 기반으로 면접방을 생성하고 첫 질문을 제공합니다.")
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    public ResponseEntity<ApiResponse<InterviewRoomCommandResponse>> createRoomAndFirstQuestion(
            @RequestBody InterviewRoomCommandRequest request,
            @AuthenticationPrincipal String userId
    ) {
        Long Id = Long.valueOf(userId);

        InterviewRoomCommandResponse response = interviewCommandService.createRoomAndFirstQuestion(
                Id,
                request.getInterviewCategory(),
                request.getDifficultyLevel(),
                request.getEvaluationStrictness()
        );
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /* 질문에 대한 답변, 답변에 대한 평가, 다음 질문 도출 */
    @Operation(summary = "면접 답변과 평가", description = "사용자의 답변을 저장하고 GPT 기반 평가 및 다음 질문을 생성합니다.")
    @Parameters({
            @Parameter(name = "roomId", description = "면접방 ID", required = true)
    })
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/{roomId}/interview")
    public ResponseEntity<ApiResponse<InterviewAnswerCommandResponse>> answerAndEvaluate(
            @PathVariable Long roomId,
            @RequestBody InterviewAnswerCommandRequest request,
            @AuthenticationPrincipal String userId
    ) {
        Long Id = Long.valueOf(userId);
        InterviewAnswerCommandResponse response = interviewCommandService.answerAndEvaluate(
                Id, roomId, request);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /* 기존의 면접방 재실행 */
    @PostMapping("/{roomId}/reexecute")
    @Operation(summary = "면접방 재실행", description = "기존 면접방의 질문을 복제하여 새 면접방을 생성합니다.")
    @Parameters({
            @Parameter(name = "roomId", description = "재실행할 기준이 되는 면접방 ID", example = "45"),
            @Parameter(name = "strictness", description = "재실행 면접의 평가 엄격도 (LENIENT | NORMAL | STRICT)", example = "NORMAL")
    })
    public ResponseEntity<ApiResponse<InterviewRoomCommandResponse>> reexecuteInterviewRoom(
            @PathVariable Long roomId,
            @RequestParam EvaluationStrictness strictness,
            @AuthenticationPrincipal String userId
    ) {
        Long Id = Long.valueOf(userId);
        InterviewRoomCommandResponse response = interviewCommandService.reexecuteInterviewRoom(Id, roomId, strictness);
        return ResponseEntity.ok(ApiResponse.success(response));
    }


    /* 면접방 삭제 */
    @Operation(summary = "면접방 삭제", description = "면접방 ID를 통해 해당 면접방을 삭제합니다.")
    @Parameters({
            @Parameter(name = "roomId", description = "삭제할 면접방 ID", required = true)
    })
    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/{roomId}")
    public ResponseEntity<ApiResponse<Void>> deleteInterviewRoom(
            @PathVariable Long roomId,
            @AuthenticationPrincipal String userId
    ) {
        Long Id = Long.valueOf(userId);
        interviewCommandService.deleteInterviewRoom(Id, roomId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /* 면접방 정보 수정 */
    @Operation(summary = "면접방 정보 수정", description = "면접방 제목 또는 메모를 수정합니다.")
    @Parameters({
            @Parameter(name = "roomId", description = "수정할 면접방 ID", required = true)
    })
    @PatchMapping("/{roomId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ApiResponse<Void>> updateInterviewRoomInfo(
            @PathVariable Long roomId,
            @RequestBody @Valid InterviewRoomUpdateCommandRequest request,
            @AuthenticationPrincipal String userId
    ) {
        Long Id = Long.valueOf(userId);
        interviewCommandService.updateInterviewRoom(Id, roomId, request);
        return ResponseEntity.ok(ApiResponse.success(null));
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

    @ExceptionHandler(InterviewRoomDeleteException.class)
    public ResponseEntity<ApiResponse<Void>> handleInterviewRoomDeleteException(InterviewRoomDeleteException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(InterviewRoomTitleInvalidException.class)
    public ResponseEntity<ApiResponse<Void>> handleInterviewRoomTitleInvalidException(InterviewRoomTitleInvalidException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }


}