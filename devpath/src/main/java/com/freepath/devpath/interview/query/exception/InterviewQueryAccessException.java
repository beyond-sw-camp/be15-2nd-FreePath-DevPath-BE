package com.freepath.devpath.interview.query.exception;

import com.freepath.devpath.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class InterviewQueryAccessException extends RuntimeException{

    private final ErrorCode errorCode;

    public InterviewQueryAccessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
