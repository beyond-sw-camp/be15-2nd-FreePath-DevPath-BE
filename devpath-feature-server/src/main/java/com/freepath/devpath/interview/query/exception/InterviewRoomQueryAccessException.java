package com.freepath.devpath.interview.query.exception;

import com.freepath.devpath.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class InterviewRoomQueryAccessException extends RuntimeException{

    private final ErrorCode errorCode;

    public InterviewRoomQueryAccessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
