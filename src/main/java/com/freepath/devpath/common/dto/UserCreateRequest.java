package com.freepath.devpath.common.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserCreateRequest {
    private final String loginId;
    private final String password;
    private final String email;
    private final String userName;
    private final String nickName;
    private final String loginMethod;
    private final String itNewsSubscription;
}
