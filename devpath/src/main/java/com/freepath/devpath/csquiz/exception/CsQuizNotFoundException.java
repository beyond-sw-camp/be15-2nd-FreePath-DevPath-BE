package com.freepath.devpath.csquiz.exception;

import com.freepath.devpath.common.exception.ErrorCode;

public class CsQuizNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public CsQuizNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
