package com.freepath.devpath.board.vote.command.exception;

import com.freepath.devpath.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class VoteParticipateFailedException extends RuntimeException {
    private final ErrorCode errorCode;

    public VoteParticipateFailedException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}