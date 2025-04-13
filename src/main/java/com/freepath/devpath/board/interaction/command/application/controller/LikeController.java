package com.freepath.devpath.board.interaction.command.application.controller;

import com.freepath.devpath.board.interaction.command.application.dto.*;
import com.freepath.devpath.board.interaction.command.application.service.LikeService;
import com.freepath.devpath.board.interaction.exception.*;
import com.freepath.devpath.common.dto.ApiResponse;
import com.freepath.devpath.common.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like")
@RequiredArgsConstructor
@Tag(name = "좋아요", description = "게시글/댓글 좋아요 및 취소 관련 API")
public class LikeController {

    private final LikeService likeService;

    @Operation(
            summary = "좋아요 등록",
            description = "게시글 또는 댓글 중 하나에 좋아요를 등록합니다. 둘 중 하나만 보내야 합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = LikeRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "게시글 좋아요",
                                            summary = "게시글에 좋아요",
                                            value = "{ \"boardId\": 1 }"
                                    ),
                                    @ExampleObject(
                                            name = "댓글 좋아요",
                                            summary = "댓글에 좋아요",
                                            value = "{ \"commentId\": 2 }"
                                    )
                            }
                    )
            )
    )
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> like(@RequestBody LikeRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(authentication.getName());

        likeService.like(userId, request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(
            summary = "좋아요 취소",
            description = "게시글 또는 댓글 중 하나에 등록된 좋아요를 취소합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = LikeRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "게시글 좋아요 취소",
                                            summary = "게시글 좋아요를 취소합니다.",
                                            value = "{ \"boardId\": 1 }"
                                    ),
                                    @ExampleObject(
                                            name = "댓글 좋아요 취소",
                                            summary = "댓글 좋아요를 취소합니다.",
                                            value = "{ \"commentId\": 2 }"
                                    )
                            }
                    )
            )
    )
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> unlike(@RequestBody LikeRequest request) {
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
