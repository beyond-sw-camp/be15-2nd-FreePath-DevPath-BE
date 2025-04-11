package com.freepath.devpath.interview.query.controller;

import com.freepath.devpath.common.dto.ApiResponse;
import com.freepath.devpath.interview.query.dto.InterviewRoomDto;
import com.freepath.devpath.interview.query.service.InterviewQueryAdminService;
import com.freepath.devpath.interview.query.service.InterviewQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/interview-room")
@RequiredArgsConstructor
public class InterviewAdminQueryController {

    private final InterviewQueryAdminService interviewQueryAdminService;

    /* 모든 유저에 대한 면접방 목록 조회 */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<InterviewRoomDto>>> getAllInterviewRooms() {
        List<InterviewRoomDto> response = interviewQueryAdminService.getAllInterviewRooms();
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
