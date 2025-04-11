package com.freepath.devpath.board.post.query.exception;

import com.freepath.devpath.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class InvalidDateIntervalException extends RuntimeException {
    private final ErrorCode errorCode;

    public InvalidDateIntervalException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}