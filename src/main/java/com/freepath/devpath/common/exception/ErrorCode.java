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
    PASSWORD_NOT_MATCHED("10002", "비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),

    // 게시판 관련 오류
    POST_NOT_FOUND("20001", "해당 게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    POST_CREATION_FAILED("20002", "게시글 생성에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    POST_UPDATE_FAILED("20003", "게시글 수정에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    POST_UPDATE_FORBIDDEN("20004", "게시글을 작성한 사용자의 요청이 아닙니다.", HttpStatus.FORBIDDEN),
    POST_DELETE_FAILED("20005", "게시글 삭제에 실패했습니다.", HttpStatus.NOT_FOUND),
    FILE_DELETE_FAILED("20006", "첨부파일 삭제에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    POST_DELETE_FORBIDDEN("20007","게시글을 작성한 사용자의 요청이 아닙니다." , HttpStatus.FORBIDDEN),
    POST_ALREADY_DELETED("20008", "이미 삭제된 게시글입니다.", HttpStatus.GONE),


    //채팅 관련 오류
    USER_ALREADY_BLOCKED("60001","이미 차단한 사용자입니다.",HttpStatus.CONFLICT),
    USER_NOT_BLOCKED("60002","차단하지 않은 사용자입니다.",HttpStatus.NOT_FOUND),

    // 공통 오류
    VALIDATION_ERROR("90001", "입력 값 검증 오류입니다.", HttpStatus.BAD_REQUEST),
    UNKNOWN_RUNTIME_ERROR("90002", "알 수 없는 런타임 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    UNKNOWN_ERROR("90003", "알 수 없는 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
