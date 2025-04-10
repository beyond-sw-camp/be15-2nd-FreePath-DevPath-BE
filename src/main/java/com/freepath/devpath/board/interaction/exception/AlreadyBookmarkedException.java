package com.freepath.devpath.board.interaction.exception;

import com.freepath.devpath.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class AlreadyBookmarkedException extends RuntimeException {
    private final ErrorCode errorCode;
    public AlreadyBookmarkedException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
