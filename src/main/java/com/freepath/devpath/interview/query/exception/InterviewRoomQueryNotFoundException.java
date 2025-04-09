package com.freepath.devpath.interview.query.exception;

import com.freepath.devpath.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class InterviewRoomQueryNotFoundException extends RuntimeException{

    private final ErrorCode errorCode;

    public InterviewRoomQueryNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
