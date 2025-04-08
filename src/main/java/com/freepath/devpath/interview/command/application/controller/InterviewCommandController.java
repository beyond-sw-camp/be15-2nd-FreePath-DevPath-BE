package com.freepath.devpath.interview.command.application.controller;

import com.freepath.devpath.common.dto.ApiResponse;
import com.freepath.devpath.interview.command.application.dto.request.InterviewAnswerCommandRequest;
import com.freepath.devpath.interview.command.application.dto.request.InterviewRoomCommandRequest;
import com.freepath.devpath.interview.command.application.dto.response.InterviewAnswerCommandResponse;
import com.freepath.devpath.interview.command.application.dto.response.InterviewRoomCommandResponse;
import com.freepath.devpath.interview.command.application.service.InterviewCommandService;
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
}