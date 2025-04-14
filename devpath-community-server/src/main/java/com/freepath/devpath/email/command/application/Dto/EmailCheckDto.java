package com.freepath.devpath.email.command.application.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class EmailCheckDto {
    @Email
    @NotEmpty(message = "이메일을 입력해 주세요")
    private String email;

    @NotEmpty(message = "인증 번호를 입력해 주세요")
    private String authNum;

    @NotBlank
    @Schema(description = "이메일 인증 목적 (SIGN_UP / FIND_LOGINID / RESET_PASSWORD / CHANGE_PASSWORD / CHANGE_EMAIL / DELETE_USER)",
            example = "SIGN_UP")
    private String purpose;
}