package com.freepath.devpath.report.exception;

import com.freepath.devpath.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class AlreadyCheckedReportException extends RuntimeException {
    private final ErrorCode errorCode;

    public AlreadyCheckedReportException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}