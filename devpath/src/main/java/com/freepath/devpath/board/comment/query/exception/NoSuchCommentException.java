package com.freepath.devpath.board.comment.query.exception;

import com.freepath.devpath.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class NoSuchCommentException extends RuntimeException {

    private final ErrorCode errorCode;

    public NoSuchCommentException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}