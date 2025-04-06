package com.freepath.devpath.command.controller;

import com.freepath.devpath.command.service.UserCommandService;
import com.freepath.devpath.common.dto.UserCreateRequest;
import com.freepath.devpath.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserCommandController {
    private final UserCommandService userCommandService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> registUser(@RequestBody UserCreateRequest request) {
        userCommandService.registerUser(request);

        return ResponseEntity.status(HttpStatus.CREATED) // 201 상태코드
                .body(ApiResponse.success(null));
    }
}
