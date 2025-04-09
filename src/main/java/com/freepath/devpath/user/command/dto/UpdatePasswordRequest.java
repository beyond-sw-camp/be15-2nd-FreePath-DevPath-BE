package com.freepath.devpath.user.command.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdatePasswordRequest {
    @Email
    private final String email;
    private final String newPassword;
}
