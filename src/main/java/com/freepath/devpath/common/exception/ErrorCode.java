package com.freepath.devpath.common.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum ErrorCode {
    // 회원 관련 오류
    USER_NOT_FOUND("10001", "해당 회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    PASSWORD_NOT_MATCHED("10002ww", "비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),

    // 게시판 관련 오류
    POST_NOT_FOUND("20001", "해당 게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    POST_CREATION_FAILED("20002", "게시글 생성에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    POST_UPDATE_FAILED("20003", "게시글 수정에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    POST_DELETE_FAILED("20004", "게시글 삭제에 실패했습니다.", HttpStatus.NOT_FOUND),
    FILE_DELETE_FAILED("20006", "첨부파일 삭제에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    POST_ALREADY_DELETED("20005", "이미 삭제된 게시글입니다.", HttpStatus.GONE);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
