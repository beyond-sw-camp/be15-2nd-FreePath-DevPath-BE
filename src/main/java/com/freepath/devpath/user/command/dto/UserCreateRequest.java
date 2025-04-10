package com.freepath.devpath.user.command.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserCreateRequest {
    private final String loginId;
    private final String password;
    @Email
    private final String email;
    private final String userName;
    private final String nickname;
    private final String loginMethod;
    private final String itNewsSubscription;
}
