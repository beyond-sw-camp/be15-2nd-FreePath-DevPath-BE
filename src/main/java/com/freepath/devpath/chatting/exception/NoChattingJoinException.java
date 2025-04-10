package com.freepath.devpath.chatting.exception;

import com.freepath.devpath.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class NoChattingJoinException extends RuntimeException{
    private final ErrorCode errorCode;

    public NoChattingJoinException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
