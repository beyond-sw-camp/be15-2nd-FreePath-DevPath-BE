package com.freepath.devpath.user.exception;

import lombok.Getter;

@Getter
public class UserException extends RuntimeException {
    private final UserErrorCode userErrorCode;

    public UserException(final UserErrorCode userErrorCode) {
        super(userErrorCode.getMessage());
        this.userErrorCode = userErrorCode;
    }
}
