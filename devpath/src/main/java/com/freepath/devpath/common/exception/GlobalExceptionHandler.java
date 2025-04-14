package com.freepath.devpath.common.exception;

import com.freepath.devpath.board.comment.command.exception.CommentAccessDeniedException;
import com.freepath.devpath.board.comment.command.exception.CommentDeleteDeniedException;
import com.freepath.devpath.board.comment.command.exception.CommentInvalidArgumentException;
import com.freepath.devpath.board.comment.command.exception.CommentNotFoundException;
import com.freepath.devpath.common.dto.ApiResponse;
import com.freepath.devpath.email.exception.EmailAuthException;
import com.freepath.devpath.email.exception.NewsNotFoundException;
import com.freepath.devpath.email.exception.TempUserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
        ErrorCode errorCode = ErrorCode.VALIDATION_ERROR;
        StringBuilder errorMessage = new StringBuilder(errorCode.getMessage());
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errorMessage.append(String.format(" [%s : %s]", error.getField(), error.getDefaultMessage()));
        }

        ApiResponse<Void> response
                = ApiResponse.failure(errorCode.getCode(), errorMessage.toString());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleException(RuntimeException e) {
        ErrorCode errorCode = ErrorCode.UNKNOWN_RUNTIME_ERROR;

        String detailedMessage = errorCode.getMessage() + " -> " + e.getMessage();

        ApiResponse<Void> response = ApiResponse.failure(errorCode.getCode(), detailedMessage);
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        ErrorCode errorCode = ErrorCode.UNKNOWN_ERROR;

        String detailedMessage = errorCode.getMessage() + " -> " + e.getMessage();

        ApiResponse<Void> response = ApiResponse.failure(errorCode.getCode(), detailedMessage);
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }


    @ExceptionHandler(EmailAuthException.class)
    public ResponseEntity<ApiResponse<Void>> handleEmailAuthException(EmailAuthException e) {
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getCode(), errorCode.getMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(TempUserNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleTempUserNotFoundException(TempUserNotFoundException e) {
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getCode(), errorCode.getMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(NewsNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNewsNotFoundException(NewsNotFoundException e) {
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getCode(), errorCode.getMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleCommentNotFoundException(CommentNotFoundException e) {
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getCode(), errorCode.getMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(CommentInvalidArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleCommentInvalidArgumentException(CommentInvalidArgumentException e) {
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getCode(), errorCode.getMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(CommentAccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleCommentAccessDeniedException(CommentAccessDeniedException e) {
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getCode(), errorCode.getMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(CommentDeleteDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleCommentDeleteDeniedException(CommentDeleteDeniedException e) {
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getCode(), errorCode.getMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }
}
