package com.freepath.devpath.report.exception;

import com.freepath.devpath.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class NoSuchReportCheckException extends RuntimeException {
    private final ErrorCode errorCode;

    public NoSuchReportCheckException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}