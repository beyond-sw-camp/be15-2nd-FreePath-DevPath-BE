package com.freepath.devpath.board.comment.query.controller;

import com.freepath.devpath.board.comment.query.dto.*;
import com.freepath.devpath.board.comment.query.service.CommentQueryService;
import com.freepath.devpath.common.dto.ApiResponse;
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

//    @GetMapping("/my-reported-comments")
//    public ResponseEntity<ApiResponse<MyCommentListResponse>> getMyReportedComments(
//            @ModelAttribute MyCommentSearchRequest searchRequest,
//            @AuthenticationPrincipal UserDetails userDetails) {
//
//        int userId = Integer.parseInt(userDetails.getUsername());
//        MyCommentListResponse response = commentQueryService.getMyreportedComments(searchRequest, userId);
//        return ResponseEntity.ok(ApiResponse.success(response));
//    }

    @GetMapping("/my-reported-comments")
    public ResponseEntity<ApiResponse<MyCommentListResponse>> getMyReportedComments(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetails userDetails) {

        // 요청 로그 추가
        System.out.println(">>>> Request received for /my-reported-comments");

        int userId = Integer.parseInt(userDetails.getUsername());
        MyCommentSearchRequest searchRequest = new MyCommentSearchRequest();
        searchRequest.setPage(page);
        searchRequest.setSize(size);

        MyCommentListResponse response = commentQueryService.getMyreportedComments(searchRequest, userId);

        // 성공적인 응답 로그
        System.out.println(">>>> Response sent for /my-reported-comments");

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
