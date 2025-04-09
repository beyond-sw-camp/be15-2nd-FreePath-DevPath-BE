package com.freepath.devpath.board.interaction.command.application.controller;


import com.freepath.devpath.board.interaction.command.application.dto.LikeRequest;
import com.freepath.devpath.board.interaction.command.application.service.LikeService;
import com.freepath.devpath.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> like(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails,
            @RequestBody LikeRequest request
    ) {
        Long userId = Long.valueOf(userDetails.getUsername());
        likeService.like(userId, request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> unlike(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails,
            @RequestBody LikeRequest request
    ) {
        Long userId = Long.valueOf(userDetails.getUsername());
        likeService.unlike(userId, request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
