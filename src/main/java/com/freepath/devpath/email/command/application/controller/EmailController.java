package com.freepath.devpath.email.command.application.controller;

import com.freepath.devpath.common.dto.ApiResponse;
import com.freepath.devpath.common.exception.ErrorCode;
import com.freepath.devpath.email.command.application.Dto.EmailAuthPurpose;
import com.freepath.devpath.email.command.application.Dto.EmailCheckDto;
import com.freepath.devpath.email.command.application.service.EmailService;
import com.freepath.devpath.email.exception.EmailAuthException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "실 사용자 이메일 인증", description = "사용자의 회원가입시 이메일 인증 기능 API")
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/email/check")
    @Operation(summary = "이메일과 인증번호 확인", description = "해당 이메일에 전송된 인증번호가 맞는지 확인 합니다.")
    @Parameters({
            @Parameter(
                    name = "purpose",
                    description = "이메일 인증 목적 (SIGN_UP / FIND_LOGINID / RESET_PASSWORD / CHANGE_PASSWORD / CHANGE_EMAIL / DELETE_USER)",
                    required = true
            )
    })
    public ResponseEntity<ApiResponse<String>> authCheck(@RequestBody @Valid EmailCheckDto emailCheckDto) {
        EmailAuthPurpose purpose;
        try {
            purpose = EmailAuthPurpose.valueOf(emailCheckDto.getPurpose().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new EmailAuthException(ErrorCode.INVALID_AUTH_PURPOSE);
        }

        boolean checked = emailService.checkAuthNum(
                emailCheckDto.getEmail(),
                emailCheckDto.getAuthNum(),
                purpose
        );

        if (checked) {
            return ResponseEntity.ok(ApiResponse.success("ok"));
        } else {
            throw new EmailAuthException(ErrorCode.INVALID_EMAIL_AUTH_CODE);
        }
    }
}