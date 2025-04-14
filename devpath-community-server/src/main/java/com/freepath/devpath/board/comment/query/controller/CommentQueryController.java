package com.freepath.devpath.board.comment.query.controller;

import com.freepath.devpath.board.comment.query.dto.CommentTreeDto;
import com.freepath.devpath.board.comment.query.dto.MyCommentListResponse;
import com.freepath.devpath.board.comment.query.dto.MyCommentSearchRequest;
import com.freepath.devpath.board.comment.query.exception.NoSuchCommentException;
import com.freepath.devpath.board.comment.query.service.CommentQueryService;
import com.freepath.devpath.common.dto.ApiResponse;
import com.freepath.devpath.common.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "댓글 및 대댓글 조회", description = "댓글, 대댓글을 조회 기능 API")
public class CommentQueryController {

    private final CommentQueryService commentQueryService;

    @GetMapping("/board/{boardId}/comments")
    @Operation(summary = "1개의 게시글 안에 있는 모든 댓글,대댓글 조회",
            description = "게시글 내에 있는 모든 댓글 또는 대댓글을 계층형 쿼리문을 사용하여 조회 합니다.")
    public ResponseEntity<ApiResponse<List<CommentTreeDto>>> getCommentTree(@PathVariable int boardId) {
        List<CommentTreeDto> comments = commentQueryService.getCommentsAsTree(boardId);
        return ResponseEntity.ok(ApiResponse.success(comments));
    }

    @GetMapping("/my-comments")
    @Operation(summary = "내가 쓴 댓글, 대댓글을 조회", description = "내가 쓴 댓글, 대댓글을 조회 합니다.")
    public ResponseEntity<ApiResponse<MyCommentListResponse>> getMyComments(
            MyCommentSearchRequest searchRequest,
            @AuthenticationPrincipal UserDetails userDetails) {

        int userId = Integer.parseInt(userDetails.getUsername());

        MyCommentListResponse response = commentQueryService.getMyComments(searchRequest, userId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/my-reported-comments")
    @Operation(summary = "내가 쓴 신고된 댓글, 대댓글을 조회", description = "내가 쓴 신고된 댓글, 대댓글을 조회 합니다.")
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
