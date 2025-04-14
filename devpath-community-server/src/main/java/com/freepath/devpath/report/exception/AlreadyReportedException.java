package com.freepath.devpath.report.exception;

import com.freepath.devpath.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class AlreadyReportedException extends RuntimeException {
    private final ErrorCode errorCode;

    public AlreadyReportedException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}