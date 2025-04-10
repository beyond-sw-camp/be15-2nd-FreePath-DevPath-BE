package com.freepath.devpath.user.command.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GoogleSignUpRequest {
    private String email;
    private String nickname;
    private String itNewsSubscription;
}
