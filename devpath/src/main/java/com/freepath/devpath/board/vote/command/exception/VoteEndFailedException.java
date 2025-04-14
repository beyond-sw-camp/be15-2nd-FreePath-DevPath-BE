package com.freepath.devpath.board.vote.command.exception;

import com.freepath.devpath.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class VoteEndFailedException extends RuntimeException {
    private final ErrorCode errorCode;

    public VoteEndFailedException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}