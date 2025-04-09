package com.freepath.devpath.email.exception;

import com.freepath.devpath.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class TempUserNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public TempUserNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
