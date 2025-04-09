package com.freepath.devpath.common.auth.controller;

import com.freepath.devpath.common.auth.dto.LoginRequest;
import com.freepath.devpath.common.auth.dto.RefreshTokenRequest;
import com.freepath.devpath.common.auth.dto.TokenResponse;
import com.freepath.devpath.common.auth.service.AuthService;
import com.freepath.devpath.common.dto.ApiResponse;
import com.freepath.devpath.common.auth.dto.UserDeleteRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@RequestBody LoginRequest request) {
        TokenResponse token = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(token));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refreshToken(@RequestBody RefreshTokenRequest request) {
        TokenResponse response = authService.refreshToken(request.getRefreshToken());

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestBody RefreshTokenRequest request) {
        authService.logout(request.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @AuthenticationPrincipal User user,
            @RequestBody @Validated UserDeleteRequest request
    ) {
        String userId = user.getUsername();
        authService.deleteUser(userId, request.getPassword());

        return ResponseEntity.ok(ApiResponse.success(null));
    }
}