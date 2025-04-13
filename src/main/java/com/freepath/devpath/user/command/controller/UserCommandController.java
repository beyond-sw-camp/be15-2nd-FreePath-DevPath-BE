package com.freepath.devpath.user.command.controller;

import com.freepath.devpath.common.auth.service.AuthService;
import com.freepath.devpath.common.exception.ErrorCode;
import com.freepath.devpath.email.command.application.Dto.EmailAuthPurpose;
import com.freepath.devpath.email.command.application.Dto.EmailRequestDto;
import com.freepath.devpath.user.command.dto.*;
import com.freepath.devpath.email.command.application.service.EmailService;
import com.freepath.devpath.user.command.service.UserCommandService;
import com.freepath.devpath.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class UserCommandController {
    private final UserCommandService userCommandService;
    private final EmailService emailService;
    private final AuthService authService;

    @PostMapping("/signup/temp")
    public ResponseEntity<ApiResponse<Void>> registTempUser(@RequestBody @Validated UserCreateRequest request) {
        if (userCommandService.isUserRestricted(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.failure(ErrorCode.RESTRICTED_USER.getCode(),
                            ErrorCode.RESTRICTED_USER.getMessage()));
        }

        if(userCommandService.isEmailDuplicate(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.failure(ErrorCode.EMAIL_ALREADY_EXISTS.getCode(),
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
        log.debug("받은 이메일: {}, 목적: {}", request.getEmail(), request.getPurpose());

        if (request.getPurpose() != EmailAuthPurpose.CHANGE_EMAIL) {
            authService.validateUserStatusByEmail(request.getEmail()); // 제재 여부, 탈퇴 확인
        }

        emailService.sendCheckEmail(request.getEmail(), request.getPurpose());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(null));
    }


    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@RequestBody @Validated ResetPasswordRequest request){
        userCommandService.resetPassword(request.getEmail(), request.getLoginId(), request.getNewPassword());

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePasword(@RequestBody @Validated ChaengePasswordRequest request){
        userCommandService.changePassword(request.getEmail(), request.getCurrentPassword(), request.getNewPassword());

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/change-email")
    public ResponseEntity<ApiResponse<Void>> changeEmail(@RequestBody @Validated ChangeEmailRequest request){
        userCommandService.changeEmail(request.getCurrentEmail(), request.getNewEmail());

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/signup-social")
    public ResponseEntity<ApiResponse<Void>> socialSignUp(@RequestBody @Validated GoogleSignUpRequest request){
        userCommandService.completeSocialSignup(request.getEmail(), request.getNickname(), request.getItNewsSubscription());

        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
