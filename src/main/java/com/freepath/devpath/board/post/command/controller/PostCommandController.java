package com.freepath.devpath.board.post.command.controller;

import com.freepath.devpath.board.post.command.dto.request.PostCreateRequest;
import com.freepath.devpath.board.post.command.dto.request.PostUpdateRequest;
import com.freepath.devpath.board.post.command.exception.FileDeleteFailedException;
import com.freepath.devpath.board.post.command.exception.FileUpdateFailedException;
import com.freepath.devpath.board.post.command.exception.InvalidPostAuthorException;
import com.freepath.devpath.board.post.command.exception.NoSuchPostException;
import com.freepath.devpath.board.post.command.service.PostCommandService;
import com.freepath.devpath.board.post.command.dto.response.PostCreateResponse;
import com.freepath.devpath.common.exception.ErrorCode;
import com.freepath.devpath.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.http.HttpStatusCode;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class PostCommandController {

    private final PostCommandService postCommandService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<PostCreateResponse>> uploadPost(
            @RequestPart @Validated PostCreateRequest postCreateRequest,
            @RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        PostCreateResponse postCreateResponse = postCommandService.createPost(postCreateRequest, multipartFiles, Integer.parseInt(userDetails.getUsername()));

        return ResponseEntity
                .status(HttpStatusCode.CREATED)
                .body(ApiResponse.success(postCreateResponse));
    }

    @PutMapping("/{board-id}")
    public ResponseEntity<ApiResponse<Void>> updatePost(
            @PathVariable("board-id") int boardId,
            @RequestBody @Validated PostUpdateRequest postUpdateRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        int userId = Integer.parseInt(userDetails.getUsername());
        postCommandService.updatePost(postUpdateRequest, boardId, userId);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/{board-id}")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @PathVariable("board-id") int boardId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        int userId = Integer.parseInt(userDetails.getUsername());
        postCommandService.deletePost(boardId, userId);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // ===== 컨트롤러 레벨 예외 핸들러들 ===== //

    @ExceptionHandler(NoSuchPostException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoSuchPostException(NoSuchPostException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(InvalidPostAuthorException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidPostAuthorException(InvalidPostAuthorException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(FileDeleteFailedException.class)
    public ResponseEntity<ApiResponse<Void>> handleFileDeleteFailedException(FileDeleteFailedException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(FileUpdateFailedException.class)
    public ResponseEntity<ApiResponse<Void>> handleFileUpdateFailedException(FileUpdateFailedException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }

}
