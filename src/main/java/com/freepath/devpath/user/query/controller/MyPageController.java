package com.freepath.devpath.user.query.controller;

import com.freepath.devpath.common.dto.ApiResponse;
import com.freepath.devpath.user.query.dto.response.UserInfoResponse;
import com.freepath.devpath.user.query.service.MyPageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "마이페이지 API", description = "마이페이지 정보 조회")
@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyPageController {
    private final MyPageService myPageService;

    @Operation(summary = "내 정보 조회", description = "마이페이지에서 사용자의 기본 정보를 조회합니다")
    @GetMapping("/info")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getUserInfo(@AuthenticationPrincipal User user) {
        Integer userId = Integer.valueOf(user.getUsername());
        UserInfoResponse response = myPageService.getUserInfo(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
