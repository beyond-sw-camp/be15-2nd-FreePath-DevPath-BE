package com.freepath.devpath.user.query.controller;

import com.freepath.devpath.common.dto.ApiResponse;
import com.freepath.devpath.user.query.service.UserQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "회원 조회", description = "유저 관련 조회 기능 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserQueryController {
    private final UserQueryService userQueryService;

    @Operation(summary = "로그인 ID 찾기", description = "이메일을 통해 로그인 ID를 조회합니다")
    @PostMapping("/find-id")
    public ResponseEntity<ApiResponse<String>> findLoginId(@RequestBody Map<String, String> body){
        String email = body.get("email");
        String loginId = userQueryService.findLoginIdByEmail(email);

        return ResponseEntity.ok(ApiResponse.success(loginId));
    }
}
