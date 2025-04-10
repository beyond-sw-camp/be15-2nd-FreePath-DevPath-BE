package com.freepath.devpath.board.comment.query.controller;

import com.freepath.devpath.board.comment.query.dto.*;
import com.freepath.devpath.board.comment.query.service.CommentQueryService;
import com.freepath.devpath.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
}
