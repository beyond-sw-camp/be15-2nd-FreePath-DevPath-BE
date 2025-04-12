package com.freepath.devpath.user.command.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserCreateRequest {
    @NotBlank
    @Size(max = 12)
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "아이디는 영문 또는 숫자만 입력할 수 있습니다")
    private final String loginId;
    @NotBlank
    @Pattern(
            regexp = "^(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?`~])[\\w!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?`~]{6,12}$",
            message = "비밀번호는 6자 이상 12자 이하이며, 특수문자를 최소 1개 포함해야 합니다"
    )
    private final String password;
    @Email
    private final String email;
    private final String userName;
    private final String nickname;
    private final String loginMethod;
    private final String itNewsSubscription;
}
