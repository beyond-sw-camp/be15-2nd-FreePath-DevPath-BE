package com.freepath.devpath.board.comment.query.controller;

import com.freepath.devpath.board.comment.query.dto.*;
import com.freepath.devpath.board.comment.query.exception.NoSuchCommentException;
import com.freepath.devpath.board.comment.query.service.CommentQueryService;
import com.freepath.devpath.common.dto.ApiResponse;
import com.freepath.devpath.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentQueryController {

    private final CommentQueryService commentQueryService;

    @GetMapping("/board/{boardId}/comments")
    public ResponseEntity<ApiResponse<List<CommentTreeDto>>> getCommentTree(@PathVariable int boardId) {
        List<CommentTreeDto> comments = commentQueryService.getCommentsAsTree(boardId);
        return ResponseEntity.ok(ApiResponse.success(comments));
    }

    @GetMapping("/my-comments")
    public ResponseEntity<ApiResponse<MyCommentListResponse>> getMyComments(
            MyCommentSearchRequest searchRequest,
            @AuthenticationPrincipal UserDetails userDetails) {

        int userId = Integer.parseInt(userDetails.getUsername());

        MyCommentListResponse response = commentQueryService.getMyComments(searchRequest, userId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/my-reported-comments")
    public ResponseEntity<ApiResponse<MyCommentListResponse>> getMyReportedComments(
            @ModelAttribute MyCommentSearchRequest searchRequest,
            @AuthenticationPrincipal UserDetails userDetails) {

        int userId = Integer.parseInt(userDetails.getUsername());
        MyCommentListResponse response = commentQueryService.getMyreportedComments(searchRequest, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @ExceptionHandler(NoSuchCommentException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoSuchCommentException(NoSuchCommentException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }
}
