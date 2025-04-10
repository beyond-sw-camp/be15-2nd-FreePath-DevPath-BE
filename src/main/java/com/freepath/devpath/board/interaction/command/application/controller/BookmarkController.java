package com.freepath.devpath.board.interaction.command.application.controller;

import com.freepath.devpath.board.interaction.command.application.dto.BookmarkRequest;
import com.freepath.devpath.board.interaction.command.application.service.BookmarkService;
import com.freepath.devpath.board.interaction.exception.AlreadyBookmarkedException;
import com.freepath.devpath.board.interaction.exception.BoardNotFoundException;
import com.freepath.devpath.board.interaction.exception.BookmarkNotFoundException;
import com.freepath.devpath.common.dto.ApiResponse;
import com.freepath.devpath.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookmark")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> bookmark(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails,
            @RequestBody BookmarkRequest request
    ) {
        Long userId = Long.valueOf(userDetails.getUsername());
        bookmarkService.bookmark(userId, request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> unbookmark(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails,
            @RequestBody BookmarkRequest request
    ) {
        Long userId = Long.valueOf(userDetails.getUsername());
        bookmarkService.unbookmark(userId, request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // ===== 컨트롤러 레벨 예외 핸들러들 ===== //

    @ExceptionHandler(BoardNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleBoardNotFoundException(BoardNotFoundException e) {
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getCode(), errorCode.getMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(AlreadyBookmarkedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAlreadyBookmarkedException(AlreadyBookmarkedException e) {
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getCode(), errorCode.getMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(BookmarkNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleBookmarkNotFoundException(BookmarkNotFoundException e) {
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getCode(), errorCode.getMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }
}
