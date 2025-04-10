package com.freepath.devpath.board.post.query.controller;


import com.freepath.devpath.board.post.query.dto.request.MyPostRequest;
import com.freepath.devpath.board.post.query.dto.request.PostSearchRequest;
import com.freepath.devpath.board.post.query.dto.response.CategoryListResponse;
import com.freepath.devpath.board.post.query.dto.response.MyPostListResponse;
import com.freepath.devpath.board.post.query.dto.response.PostDetailResponse;
import com.freepath.devpath.board.post.query.dto.response.PostListResponse;
import com.freepath.devpath.board.post.query.exception.NoSuchPostException;
import com.freepath.devpath.board.post.query.service.PostQueryService;
import com.freepath.devpath.common.dto.ApiResponse;
import com.freepath.devpath.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostQueryController {

    private final PostQueryService postQueryService;

    // 단일 게시글 내용 조회
    @GetMapping("/board/{board-id}")
    public ResponseEntity<ApiResponse<PostDetailResponse>> getPost(
            @PathVariable("board-id") int boardId
    ) {
        PostDetailResponse response = postQueryService.getPostById(boardId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }


    @GetMapping("/board/category")
    public ResponseEntity<ApiResponse<PostListResponse>> getPostList(
            @ModelAttribute @Validated PostSearchRequest postSearchRequest
    ) {
        PostListResponse response = postQueryService.getPostListByCategoryId(postSearchRequest);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/board/category/{category-id}")
    public ResponseEntity<ApiResponse<CategoryListResponse>> getCategoryList(
            @PathVariable("category-id") int categoryId
    ) {
        CategoryListResponse response = postQueryService.getCategoryList(categoryId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/mypage")
    public ResponseEntity<ApiResponse<MyPostListResponse>> getMyPostList(
            @ModelAttribute @Validated MyPostRequest myPostRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        int userId = Integer.parseInt(userDetails.getUsername());
        MyPostListResponse response = postQueryService.getPostByUserId(userId, myPostRequest);

        return ResponseEntity.ok(ApiResponse.success(response));
    }


    @ExceptionHandler(NoSuchPostException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoSuchPostException(NoSuchPostException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }

}
