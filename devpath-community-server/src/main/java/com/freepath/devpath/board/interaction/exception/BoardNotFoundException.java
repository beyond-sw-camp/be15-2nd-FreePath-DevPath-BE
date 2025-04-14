package com.freepath.devpath.board.interaction.exception;


import com.freepath.devpath.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class BoardNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public BoardNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}