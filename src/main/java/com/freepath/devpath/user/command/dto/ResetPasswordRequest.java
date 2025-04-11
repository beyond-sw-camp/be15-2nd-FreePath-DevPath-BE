package com.freepath.devpath.user.command.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResetPasswordRequest {
    @Email
    @NotNull
    private final String email;
    @NotNull
    private final String loginId;
    @NotNull
    private final String newPassword;
}
