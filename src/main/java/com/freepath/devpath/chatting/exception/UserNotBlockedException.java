package com.freepath.devpath.chatting.exception;

import com.freepath.devpath.common.exception.ErrorCode;

public class UserNotBlockedException extends RuntimeException{
    private final ErrorCode errorCode;

    public UserNotBlockedException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}