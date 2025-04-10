package com.freepath.devpath.interview.query.controller;

import com.freepath.devpath.common.dto.ApiResponse;
import com.freepath.devpath.common.exception.ErrorCode;
import com.freepath.devpath.interview.query.dto.InterviewRoomDetailResponse;
import com.freepath.devpath.interview.query.dto.InterviewRoomDto;
import com.freepath.devpath.interview.query.dto.InterviewSummaryResponse;
import com.freepath.devpath.interview.query.exception.*;
import com.freepath.devpath.interview.query.service.InterviewQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interview-room")
@RequiredArgsConstructor
public class InterviewQueryController {

    private final InterviewQueryService interviewQueryService;

    /* 사용자가 면접을 진행할 카테고리 목록 조회 */
    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<String>>> getInterviewCategories() {
        List<String> categories = List.of(
                "운영체제", "네트워크", "데이터베이스","자료구조&알고리즘", "디자인 패턴"
                , "객체지향 프로그래밍", "CI/CD", "보안", "클라우드&인프라", "시스템 설계"
        );
        return ResponseEntity.ok(ApiResponse.success(categories));
    }

    /* 사용자가 진행한 면접방 목록 조회 */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<InterviewRoomDto>>> getInterviewRoomList(
            @AuthenticationPrincipal UserDetails userDetails) {

        Long userId = Long.valueOf(userDetails.getUsername());
        List<InterviewRoomDto> response = interviewQueryService.getInterviewRoomList(userId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /* 특정 카테고리에 대한 면접방 목록만 조회 */
    @GetMapping(params = "category")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ApiResponse<List<InterviewRoomDto>>> getInterviewRoomListByCategory(
            @RequestParam String category,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long userId = Long.valueOf(userDetails.getUsername());
        List<InterviewRoomDto> response = interviewQueryService.getInterviewRoomListByCategory(userId, category);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /* 면접방 정보 및 면접 이력 조회 */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{interviewRoomId}")
    public ResponseEntity<ApiResponse<InterviewRoomDetailResponse>> getInterviewRoomDetail(
            @PathVariable Long interviewRoomId,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long userId = Long.valueOf(userDetails.getUsername());
        InterviewRoomDetailResponse response = interviewQueryService.getInterviewRoomByRoomId(interviewRoomId, userId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /* 면접방의 총평 조회 */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{interviewRoomId}/summary")
    public ResponseEntity<ApiResponse<InterviewSummaryResponse>> getInterviewSummary(
            @PathVariable Long interviewRoomId,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long userId = Long.valueOf(userDetails.getUsername());
        InterviewSummaryResponse response = interviewQueryService.getInterviewSummary(interviewRoomId, userId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }


    // ===== 컨트롤러 레벨 예외 핸들러들 ===== //

    @ExceptionHandler(InterviewRoomQueryCreationException.class)
    public ResponseEntity<ApiResponse<Void>> handleInterviewRoomQueryCreationException(InterviewRoomQueryCreationException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(InterviewRoomQueryNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleInterviewRoomQueryNotFoundException(InterviewRoomQueryNotFoundException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(InterviewQueryAccessException.class)
    public ResponseEntity<ApiResponse<Void>> handleInterviewQueryAccessException(InterviewQueryAccessException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(InterviewQueryHistoryNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleInterviewQueryHistoryNotFoundException(InterviewQueryHistoryNotFoundException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(InterviewRoomQueryAccessException.class)
    public ResponseEntity<ApiResponse<Void>> handleInterviewRoomQueryAccessException(InterviewRoomQueryAccessException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }

}