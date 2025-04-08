package com.freepath.devpath.interview.command.exception;

import com.freepath.devpath.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class InterviewQuestionCreationException extends RuntimeException{

    private final ErrorCode errorCode;

    public InterviewQuestionCreationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
