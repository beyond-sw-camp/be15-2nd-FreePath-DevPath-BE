package com.freepath.devpath.user.command.controller;

import com.freepath.devpath.common.auth.dto.LoginRequest;
import com.freepath.devpath.common.auth.dto.TokenResponse;
import com.freepath.devpath.common.auth.service.AuthService;
import com.freepath.devpath.common.exception.ErrorCode;
import com.freepath.devpath.email.command.application.Dto.EmailRequestDto;
import com.freepath.devpath.user.command.dto.*;
import com.freepath.devpath.email.command.application.service.EmailService;
import com.freepath.devpath.user.command.repository.UserCommandRepository;
import com.freepath.devpath.user.command.service.UserCommandService;
import com.freepath.devpath.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserCommandController {
    private final UserCommandService userCommandService;
    private final EmailService emailService;
    private final AuthService authService;

    @PostMapping("/signup/temp")
    public ResponseEntity<ApiResponse<Void>> registTempUser(@RequestBody @Validated UserCreateRequest request) {
        if (userCommandService.isEmailDuplicate(request.getEmail())) { // 이미 존재하는 이메일인지 확인 후 인증 절차 진행
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.failure( ErrorCode.EMAIL_ALREADY_EXISTS.getCode(),
                            ErrorCode.EMAIL_ALREADY_EXISTS.getMessage()));
        }

        if (userCommandService.isLoginIdDuplicate(request.getLoginId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.failure(ErrorCode.LOGIN_ID_ALREADY_EXISTS.getCode(),
                            ErrorCode.LOGIN_ID_ALREADY_EXISTS.getMessage()));
        }

        if(userCommandService.isNicknameDuplicate(request.getNickname())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.failure(ErrorCode.NICKNAME_ALREADY_USED.getCode(),
                            ErrorCode.LOGIN_ID_ALREADY_EXISTS.getMessage()));
        }

        userCommandService.saveTempUser(request);                       // 1. 유저 정보 Redis에 임시 저장
        emailService.joinEmail(request.getEmail());                     // 2. 입력된 이메일로 인증번호 발송
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(null));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> registerUser(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        userCommandService.registerUserFromRedis(email);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(null));
    }

    @PutMapping("/info")
    public ResponseEntity<ApiResponse<Void>> modifyUser(
            @RequestBody @Validated UserModifyRequest request,
            @AuthenticationPrincipal User user) {

        Integer userId = Integer.valueOf(user.getUsername()); // 이제 username 은 userId
        userCommandService.modifyUser(request, userId);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<Void>> findLoginIdTemp(@RequestBody @Valid EmailRequestDto request) {
        authService.validateUserStatusByEmail(request.getEmail()); // 제재 여부, 탈퇴 확인
        emailService.sendCheckEmail(request.getEmail(), request.getPurpose());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(null));
    }

    @PostMapping("/update-password")
    public ResponseEntity<ApiResponse<Void>> updatePassword(@RequestBody @Validated UpdatePasswordRequest request){
        userCommandService.updatePassword(request.getEmail(), request.getLoginId(), request.getNewPassword());

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/update-email")
    public ResponseEntity<ApiResponse<Void>> updateEmail(@RequestBody @Validated UpdateEmailRequest request){
        userCommandService.updateEmail(request.getEmail(), request.getNewEmail());

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/signup-social")
    public ResponseEntity<ApiResponse<Void>> socialSignUp(@RequestBody @Validated GoogleSignUpRequest request){
        userCommandService.completeSocialSignup(request.getEmail(), request.getNickname(), request.getItNewsSubscription());

        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
