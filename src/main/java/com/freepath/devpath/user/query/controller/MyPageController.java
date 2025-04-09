package com.freepath.devpath.user.query.controller;

import com.freepath.devpath.common.dto.ApiResponse;
import com.freepath.devpath.user.query.dto.response.UserInfoResponse;
import com.freepath.devpath.user.query.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyPageController {
    private final MyPageService myPageService;

    @GetMapping("/info")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getUserInfo(@AuthenticationPrincipal User user) {
        Integer userId = Integer.valueOf(user.getUsername());

        UserInfoResponse response = myPageService.getUserInfo(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
