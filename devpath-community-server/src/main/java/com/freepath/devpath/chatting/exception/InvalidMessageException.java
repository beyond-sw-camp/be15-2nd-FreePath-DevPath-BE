package com.freepath.devpath.chatting.exception;

import com.freepath.devpath.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class InvalidMessageException extends RuntimeException{
    private final ErrorCode errorCode;

    public InvalidMessageException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}

