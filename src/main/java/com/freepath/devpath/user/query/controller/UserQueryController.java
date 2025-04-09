package com.freepath.devpath.user.query.controller;

import com.freepath.devpath.common.auth.service.AuthService;
import com.freepath.devpath.common.dto.ApiResponse;
import com.freepath.devpath.email.command.application.Dto.EmailRequestDto;
import com.freepath.devpath.email.command.application.service.EmailService;
import com.freepath.devpath.user.query.service.UserQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserQueryController {
    private final UserQueryService userQueryService;
    private final EmailService emailService;
    private final AuthService authService;

    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<Void>> findLoginIdTemp(@RequestBody @Valid EmailRequestDto request) {
        authService.validateUserStatusByEmail(request.getEmail()); // 제재 여부, 탈퇴 확인
            emailService.sendCheckEmail(request.getEmail(), request.getPurpose());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(null));
    }


    @PostMapping("/find-id")
    public ResponseEntity<ApiResponse<String>> findLoginId(@RequestBody String email){
        String loginId = userQueryService.findLoginIdByEmail(email);

        return ResponseEntity.ok(ApiResponse.success(loginId));
    }
}
