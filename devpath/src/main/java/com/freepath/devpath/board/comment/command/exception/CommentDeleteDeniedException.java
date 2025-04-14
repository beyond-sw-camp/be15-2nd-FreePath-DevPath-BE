package com.freepath.devpath.board.comment.command.exception;

import com.freepath.devpath.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class CommentDeleteDeniedException extends RuntimeException {

    private final ErrorCode errorCode;

    public CommentDeleteDeniedException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}