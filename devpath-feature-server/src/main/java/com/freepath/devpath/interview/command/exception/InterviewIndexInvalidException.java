package com.freepath.devpath.interview.command.exception;

import com.freepath.devpath.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class InterviewIndexInvalidException extends RuntimeException{

    private final ErrorCode errorCode;

    public InterviewIndexInvalidException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
