package com.freepath.devpath.email.command.application.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailRequestDto {

    @Email
    @NotEmpty(message = "이메일을 입력해 주세요")
    private String email;

    @NotNull
    private EmailAuthPurpose purpose;
}