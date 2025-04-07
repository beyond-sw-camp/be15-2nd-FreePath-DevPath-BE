package com.freepath.devpath.user.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum UserErrorCode {
    // 회원 관련 오류
    USER_NOT_FOUND("404", "해당 회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    PASSWORD_NOT_MATCHED("401", "현재 비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
