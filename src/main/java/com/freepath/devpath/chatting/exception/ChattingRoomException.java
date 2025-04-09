package com.freepath.devpath.chatting.exception;

import com.freepath.devpath.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class ChattingRoomException extends RuntimeException{
    private final ErrorCode errorCode;
    public ChattingRoomException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
