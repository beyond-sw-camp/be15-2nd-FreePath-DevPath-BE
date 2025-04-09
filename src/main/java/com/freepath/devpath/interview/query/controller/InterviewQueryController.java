package com.freepath.devpath.interview.query.controller;

import com.freepath.devpath.common.dto.ApiResponse;
import com.freepath.devpath.interview.query.dto.InterviewRoomDetailResponse;
import com.freepath.devpath.interview.query.dto.InterviewRoomDto;
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

    /* 사용자가 진행한 면접방 목록 조회 */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<InterviewRoomDto>>> getInterviewRoomList(
            @AuthenticationPrincipal UserDetails userDetails) {

        Long userId = Long.valueOf(userDetails.getUsername());
        List<InterviewRoomDto> response = interviewQueryService.getInterviewRoomList(userId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /* 면접방 정보 및 면접 이력 조회 */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{interviewRoomId}")
    public ResponseEntity<ApiResponse<InterviewRoomDetailResponse>> getInterviewRoomDetail(
            @PathVariable Long interviewRoomId,
            @AuthenticationPrincipal UserDetails userDetails) {

        InterviewRoomDetailResponse response = interviewQueryService.getInterviewRoomByRoomId(interviewRoomId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

}