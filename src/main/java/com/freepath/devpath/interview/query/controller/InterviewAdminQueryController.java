package com.freepath.devpath.interview.query.controller;

import com.freepath.devpath.common.dto.ApiResponse;
import com.freepath.devpath.interview.query.dto.InterviewRoomDto;
import com.freepath.devpath.interview.query.dto.InterviewRoomListResponse;
import com.freepath.devpath.interview.query.service.InterviewQueryAdminService;
import com.freepath.devpath.interview.query.service.InterviewQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<ApiResponse<InterviewRoomListResponse>> getAllInterviewRooms(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        InterviewRoomListResponse response = interviewQueryAdminService.getAllInterviewRooms(page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /* 모든 유저의 면접방 목록에 필터링 적용 조회 */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<InterviewRoomListResponse>> getAdminFilteredInterviewRoomList(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String difficultyLevel,
            @RequestParam(required = false) String evaluationStrictness,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        InterviewRoomListResponse response = interviewQueryAdminService.getAdminFilteredInterviewRoomList(
                status, category, difficultyLevel, evaluationStrictness, page, size);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
