package com.freepath.devpath.user.command.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GoogleSignUpRequest {
    @Email
    private String email;
    @NotNull
    private String nickname;
    @NotNull
    private String itNewsSubscription;
}
