package com.freepath.devpath.user.command.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserModifyRequest {
    private final String currentPassword;
    private final String newPassword;
    @Email
    private final String email;
    private final String nickname;
}
