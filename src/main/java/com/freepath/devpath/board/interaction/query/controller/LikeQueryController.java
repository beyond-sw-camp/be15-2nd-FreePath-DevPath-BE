package com.freepath.devpath.board.interaction.query.controller;

import com.freepath.devpath.board.interaction.query.service.LikeQueryService;
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
public class LikeQueryController {
    private final LikeQueryService likeQueryService;

    // 좋아요한 게시글 모아보기 (최신순)
    @GetMapping("/like")
    public ResponseEntity<ApiResponse<List<PostDto>>> getLikedPosts(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails
    ) {
        int userId = Integer.parseInt(userDetails.getUsername());
        List<PostDto> likedPosts = likeQueryService.getLikedPosts(userId);
        return ResponseEntity.ok(ApiResponse.success(likedPosts));
    }




}
