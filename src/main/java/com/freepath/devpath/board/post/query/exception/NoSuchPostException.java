package com.freepath.devpath.board.post.query.exception;

import com.freepath.devpath.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class NoSuchPostException extends RuntimeException {
    private final ErrorCode errorCode;

    public NoSuchPostException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}