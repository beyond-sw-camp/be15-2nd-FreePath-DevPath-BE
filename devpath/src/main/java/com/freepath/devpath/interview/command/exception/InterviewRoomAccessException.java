package com.freepath.devpath.interview.command.exception;

import com.freepath.devpath.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class InterviewRoomAccessException extends RuntimeException{

    private final ErrorCode errorCode;

    public InterviewRoomAccessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
