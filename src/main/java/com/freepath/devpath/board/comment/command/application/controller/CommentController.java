package com.freepath.devpath.board.comment.command.application.controller;

import com.freepath.devpath.board.comment.command.domain.domain.Comment;
import com.freepath.devpath.board.comment.command.application.dto.CommentRequestDto;
import com.freepath.devpath.board.comment.command.application.service.CommentCommandService;
import com.freepath.devpath.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentCommandService commentCommandService;

    @PostMapping
    public ResponseEntity<ApiResponse<Comment>> createComment(
            @RequestBody CommentRequestDto dto,
            @AuthenticationPrincipal User userDetails) {

        int userId = Integer.parseInt(userDetails.getUsername());

       Comment comment = commentCommandService.saveComment(dto, userId);

        return ResponseEntity.ok(ApiResponse.success(comment));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> updateComment(
            @PathVariable int commentId,
            @RequestBody CommentRequestDto dto,
            @AuthenticationPrincipal User userDetails) {

        int userId = Integer.parseInt(userDetails.getUsername());
        commentCommandService.updateComment(commentId, dto.getContents(), userId);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable int commentId,
            @AuthenticationPrincipal User userDetails) {

        int userId = Integer.parseInt(userDetails.getUsername());
        commentCommandService.deleteComment(commentId, userId);

        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
