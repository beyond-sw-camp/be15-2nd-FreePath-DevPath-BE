package com.freepath.devpath.board.post.command.exception;

import com.freepath.devpath.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class FileUpdateFailedException extends RuntimeException {
    private final ErrorCode errorCode;

    public FileUpdateFailedException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}