package com.freepath.devpath.interview.query.exception;

import com.freepath.devpath.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class InterviewQueryHistoryNotFoundException extends RuntimeException{

    private final ErrorCode errorCode;

    public InterviewQueryHistoryNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
