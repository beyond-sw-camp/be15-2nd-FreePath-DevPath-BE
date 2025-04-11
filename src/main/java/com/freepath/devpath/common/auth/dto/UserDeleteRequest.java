package com.freepath.devpath.common.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserDeleteRequest {
    private final String refreshToken;
    @NotBlank
    private String email;
}
