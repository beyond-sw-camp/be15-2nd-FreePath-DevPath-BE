package com.freepath.devpath.user.command.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GoogleSignUpRequest {
    @Email
    private String email;
    @NotBlank
    private String nickname;
    @NotBlank
    private String itNewsSubscription;
}
