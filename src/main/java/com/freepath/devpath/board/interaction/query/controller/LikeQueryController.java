package com.freepath.devpath.board.interaction.query.controller;

import com.freepath.devpath.board.interaction.exception.BoardNotFoundException;
import com.freepath.devpath.board.interaction.exception.CommentNotFoundException;
import com.freepath.devpath.board.interaction.query.dto.LikedBoardSearchRequest;
import com.freepath.devpath.board.interaction.query.service.LikeQueryService;
import com.freepath.devpath.board.post.query.dto.response.PostListResponse;
import com.freepath.devpath.common.dto.ApiResponse;
import com.freepath.devpath.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class LikeQueryController {
    private final LikeQueryService likeQueryService;

    //내가 좋아요한 글 모아보기 (최신순)
    @GetMapping("/mypage/like")
    public ResponseEntity<ApiResponse<PostListResponse>> getLikedPosts(
            @ModelAttribute LikedBoardSearchRequest request
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userId = Integer.parseInt(authentication.getName());

        PostListResponse response = likeQueryService.getLikedPosts(userId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 게시글 좋아요 여부 조회
    @GetMapping("/board/{boardId}/like")
    public ResponseEntity<ApiResponse<Boolean>> hasUserLikedBoard(
            @PathVariable int boardId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userId = Integer.parseInt(authentication.getName());
        boolean liked = likeQueryService.hasUserLikedBoard(userId, boardId);
        return ResponseEntity.ok(ApiResponse.success(liked));
    }


    // 게시글 좋아요 개수 조회
    @GetMapping("/board/{boardId}/like/count")
    public ResponseEntity<ApiResponse<Integer>> countLikesByBoardId(
            @PathVariable int boardId
    ) {
        int count = likeQueryService.countLikesByBoardId(boardId);
        return ResponseEntity.ok(ApiResponse.success(count));
    }

    // 댓글 좋아요 여부 조회
    @GetMapping("/board/{boardId}/comment/{commentId}/like")
    public ResponseEntity<ApiResponse<Boolean>> hasUserLikedComment(
            @PathVariable int boardId,
            @PathVariable int commentId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userId = Integer.parseInt(authentication.getName());
        boolean liked = likeQueryService.hasUserLikedComment(userId, commentId);
        return ResponseEntity.ok(ApiResponse.success(liked));
    }

    // 댓글 좋아요 개수 조회
    @GetMapping("/comment/{commentId}/like/count")
    public ResponseEntity<ApiResponse<Integer>> countLikesByCommentId(
            @PathVariable int commentId
    ) {
        int count = likeQueryService.countLikesByCommentId(commentId);
        return ResponseEntity.ok(ApiResponse.success(count));
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

}


