package com.freepath.devpath.board.interaction.command.application.controller;


import com.freepath.devpath.board.interaction.command.application.dto.LikeRequest;
import com.freepath.devpath.board.interaction.command.application.service.LikeService;
import com.freepath.devpath.board.interaction.exception.*;
import com.freepath.devpath.common.dto.ApiResponse;
import com.freepath.devpath.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    // 게시글이나 댓글 좋아요
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> like(
            @RequestBody LikeRequest request
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(authentication.getName());

        likeService.like(userId, request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // 게시글이나 댓글 좋아요 취소
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> unlike(
            @RequestBody LikeRequest request
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(authentication.getName());

        likeService.unlike(userId, request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // ===== 컨트롤러 레벨 예외 핸들러들 ===== //

    @ExceptionHandler(BoardNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleBoardNotFoundException(BoardNotFoundException e) {
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

    @ExceptionHandler(AlreadyLikedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAlreadyLikedException(AlreadyLikedException e) {
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getCode(), errorCode.getMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(LikeNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleLikeNotFoundException(LikeNotFoundException e) {
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getCode(), errorCode.getMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(CannotLikeBothException.class)
    public ResponseEntity<ApiResponse<Void>> handleCannotLikeBothException(CannotLikeBothException e) {
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getCode(), errorCode.getMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(InvalidLikeTargetException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidLikeTargetException(InvalidLikeTargetException e) {
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getCode(), errorCode.getMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }


}
