package com.freepath.devpath.board.interaction.query.controller;


import com.freepath.devpath.board.interaction.query.service.BookmarkQueryService;
import com.freepath.devpath.board.post.query.dto.response.PostDto;
import com.freepath.devpath.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class BookmarkQueryController {

    private final BookmarkQueryService bookmarkQueryService;

    // 북마크한 게시글 모아보기 (최신순)
    @GetMapping("/bookmark")
    public ResponseEntity<ApiResponse<List<PostDto>>> getBookmarkedPosts(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails
    ) {
        int userId = Integer.parseInt(userDetails.getUsername());
        List<PostDto> bookmarkedPosts = bookmarkQueryService.getBookmarkedPosts(userId);
        return ResponseEntity.ok(ApiResponse.success(bookmarkedPosts));
    }

}
