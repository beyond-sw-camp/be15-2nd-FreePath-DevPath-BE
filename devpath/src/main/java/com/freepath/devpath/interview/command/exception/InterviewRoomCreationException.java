package com.freepath.devpath.interview.command.exception;

import com.freepath.devpath.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class InterviewRoomCreationException extends RuntimeException{

    private final ErrorCode errorCode;

    public InterviewRoomCreationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
