package com.freepath.devpath.user.command.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChaengePasswordRequest {

    @Email
    private final String email;
    @NotNull
    private final String currentPassword;
    @NotNull
    private final String newPassword;
}
