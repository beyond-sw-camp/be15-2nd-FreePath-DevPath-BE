package com.freepath.devpath.board.post.query.controller;


import com.freepath.devpath.board.post.query.dto.request.MyPostRequest;
import com.freepath.devpath.board.post.query.dto.request.PostContentSearchRequest;
import com.freepath.devpath.board.post.query.dto.request.PostSearchRequest;
import com.freepath.devpath.board.post.query.dto.response.CategoryListResponse;
import com.freepath.devpath.board.post.query.dto.response.MyPostListResponse;
import com.freepath.devpath.board.post.query.dto.response.PostDetailResponse;
import com.freepath.devpath.board.post.query.dto.response.PostListResponse;
import com.freepath.devpath.board.post.query.exception.InvalidDateIntervalException;
import com.freepath.devpath.board.post.query.exception.NoSuchPostException;
import com.freepath.devpath.board.post.query.service.PostQueryService;
import com.freepath.devpath.common.dto.ApiResponse;
import com.freepath.devpath.common.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
@Tag(name = "게시글 조회", description = "게시글 단일 조회, 검색, 카테고리별 조회 기능 API")
public class PostQueryController {

    private final PostQueryService postQueryService;

    @GetMapping("/{board-id}")
    @Operation(summary = "단일 게시글 조회", description = "게시글 ID를 기반으로 게시글 상세 내용을 조회합니다.")
    public ResponseEntity<ApiResponse<PostDetailResponse>> getPost(
            @Parameter(description = "게시글 ID", required = true)
            @PathVariable("board-id") int boardId
    ) {
        PostDetailResponse response = postQueryService.getPostById(boardId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/search")
    @Operation(summary = "게시글 검색", description = "검색 조건에 따라 게시글 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<PostListResponse>> getPostList(
            @Parameter(description = "게시글 검색 조건")
            @ModelAttribute @Validated PostSearchRequest postSearchRequest
    ) {
        PostListResponse response = postQueryService.searchPostList(postSearchRequest);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/category/{category-id}")
    @Operation(summary = "카테고리별 게시글 조회", description = "카테고리 ID에 해당하는 게시글 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<CategoryListResponse>> getCategoryList(
            @Parameter(description = "카테고리 ID", required = true)
            @PathVariable("category-id") int categoryId
    ) {
        CategoryListResponse response = postQueryService.getCategoryList(categoryId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/mypage")
    @Operation(summary = "내가 작성한 게시글 조회", description = "현재 로그인한 사용자가 작성한 게시글 목록을 조회합니다. 신고된 게시글의 경우 내용은 \"신고 처리된 게시글입니다\"로 대체됩니다.")
    public ResponseEntity<ApiResponse<MyPostListResponse>> getMyPostList(
            @Parameter(description = "페이지 정보 등 요청 조건")
            @ModelAttribute @Validated MyPostRequest myPostRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        int userId = Integer.parseInt(userDetails.getUsername());
        MyPostListResponse response = postQueryService.getPostByUserId(userId, myPostRequest);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/mypage/reported-post")
    @Operation(summary = "내 신고된 게시글 조회", description = "현재 로그인한 사용자가 작성한 신고된 게시글 목록을 조회합니다. 내용은 모두 \"신고 처리된 게시글입니다\"로 대체됩니다.")
    public ResponseEntity<ApiResponse<MyPostListResponse>> getMyReportedPostList(
            @Parameter(description = "페이지 정보 등 요청 조건")
            @ModelAttribute @Validated MyPostRequest myPostRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        int userId = Integer.parseInt(userDetails.getUsername());
        MyPostListResponse response = postQueryService.getReportedPostListByUserId(userId, myPostRequest);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/search/content")
    @Operation(summary = "게시글 내용 키워드 검색", description = "게시글 내용에 포함된 키워드를 기준으로 게시글을 검색합니다.")
    public ResponseEntity<ApiResponse<PostListResponse>> getPostListByContent(
            @Parameter(description = "게시글 내용 키워드 검색 조건")
            @ModelAttribute @Validated PostContentSearchRequest postContentSearchRequest
    ) {
        PostListResponse response = postQueryService.searchBoardContentByKeyword(postContentSearchRequest);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @ExceptionHandler(NoSuchPostException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoSuchPostException(NoSuchPostException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(InvalidDateIntervalException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidDateIntervalException(InvalidDateIntervalException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }

}
