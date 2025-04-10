package com.freepath.devpath.board.interaction.exception;

import com.freepath.devpath.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class BookmarkNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;
    public BookmarkNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
