package com.freepath.devpath.user.command.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResetPasswordRequest {
    @Email
    @NotBlank
    private final String email;
    @NotBlank
    private final String loginId;
    @NotBlank
    @Pattern(
            regexp = "^(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?`~])[\\w!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?`~]{6,12}$",
            message = "비밀번호는 6자 이상 12자 이하이며, 특수문자를 최소 1개 포함해야 합니다"
    )
    private final String newPassword;
}
