package com.freepath.devpath.user.query.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserInfoRequest {
    @Email
    private final String email;
    private final String loginId;
}
