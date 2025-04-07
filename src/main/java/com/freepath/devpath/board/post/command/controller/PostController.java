package com.freepath.devpath.board.post.command.controller;

import com.freepath.devpath.board.post.command.dto.PostCreateRequest;
import com.freepath.devpath.board.post.command.service.PostService;
import com.freepath.devpath.board.post.command.dto.PostCreateResponse;
import com.freepath.devpath.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.http.HttpStatusCode;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class PostController {

    private final PostService postService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<PostCreateResponse>> uploadPost(
            @RequestPart PostCreateRequest postCreateRequest,
            @RequestPart("files") List<MultipartFile> multipartFiles,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        PostCreateResponse postCreateResponse = postService.createPost(postCreateRequest, multipartFiles, Integer.parseInt(userDetails.getUsername()));

        return ResponseEntity
                .status(HttpStatusCode.CREATED)
                .body(ApiResponse.success(postCreateResponse));
    }
}
