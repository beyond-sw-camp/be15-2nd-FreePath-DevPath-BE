package com.freepath.devpath.user.command.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChaengePasswordRequest {

    @Email
    private final String email;
    @NotBlank
    private final String currentPassword;
    @NotBlank
    @Pattern(
            regexp = "^(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?`~])[\\w!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?`~]{6,12}$",
            message = "비밀번호는 6자 이상 12자 이하이며, 특수문자를 최소 1개 포함해야 합니다"
    )
    private final String newPassword;
}
