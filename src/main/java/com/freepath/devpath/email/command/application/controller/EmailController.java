package com.freepath.devpath.email.command.application.controller;

import com.freepath.devpath.common.dto.ApiResponse;
import com.freepath.devpath.common.exception.ErrorCode;
import com.freepath.devpath.email.command.application.Dto.EmailAuthPurpose;
import com.freepath.devpath.email.command.application.Dto.EmailCheckDto;
import com.freepath.devpath.email.command.application.service.EmailService;
import com.freepath.devpath.email.exception.EmailAuthException;
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
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/email/check")
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