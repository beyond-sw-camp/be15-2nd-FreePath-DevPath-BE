package com.freepath.devpath.user.command.controller;

import com.freepath.devpath.common.auth.service.AuthService;
import com.freepath.devpath.common.exception.ErrorCode;
import com.freepath.devpath.email.command.application.Dto.EmailAuthPurpose;
import com.freepath.devpath.email.command.application.Dto.EmailRequestDto;
import com.freepath.devpath.user.command.dto.*;
import com.freepath.devpath.email.command.application.service.EmailService;
import com.freepath.devpath.user.command.service.UserCommandService;
import com.freepath.devpath.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "회원", description = "회원가입, 정보 수정, 비밀번호/이메일 변경 등의 기능 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserCommandController {

    private final UserCommandService userCommandService;
    private final EmailService emailService;
    private final AuthService authService;

    @Operation(summary = "임시 회원가입", description = "이메일 인증을 위한 임시 회원가입을 수행합니다")
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
                            ErrorCode.NICKNAME_ALREADY_USED.getMessage()));
        }

        userCommandService.saveTempUser(request);
        emailService.joinEmail(request.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(null));
    }

    @Operation(summary = "회원가입 완료", description = "이메일 인증 완료 후 실제 회원가입을 수행합니다")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> registerUser(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        userCommandService.registerUserFromRedis(email);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(null));
    }

    @Operation(summary = "회원 정보 수정", description = "닉네임, IT 뉴스 구독 여부를 수정합니다")
    @PutMapping("/info")
    public ResponseEntity<ApiResponse<Void>> modifyUser(
            @RequestBody @Validated UserModifyRequest request,
            @AuthenticationPrincipal User user) {

        Integer userId = Integer.valueOf(user.getUsername());
        userCommandService.modifyUser(request, userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "이메일 인증 요청", description = "회원가입, 이메일 변경 등을 위한 이메일 인증을 요청합니다")
    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<Void>> findLoginIdTemp(@RequestBody @Valid EmailRequestDto request) {
        log.debug("받은 이메일: {}, 목적: {}", request.getEmail(), request.getPurpose());

        if (request.getPurpose() != EmailAuthPurpose.CHANGE_EMAIL) {
            authService.validateUserStatusByEmail(request.getEmail());
        }

        emailService.sendCheckEmail(request.getEmail(), request.getPurpose());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(null));
    }

    @Operation(summary = "비밀번호 재설정", description = "비밀번호 찾기를 통해 비밀번호를 재설정합니다")
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@RequestBody @Validated ResetPasswordRequest request){
        userCommandService.resetPassword(request.getEmail(), request.getLoginId(), request.getNewPassword());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "비밀번호 변경", description = "로그인 후 비밀번호를 변경합니다")
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePasword(@RequestBody @Validated ChaengePasswordRequest request){
        userCommandService.changePassword(request.getEmail(), request.getCurrentPassword(), request.getNewPassword());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "이메일 변경", description = "기존 이메일을 새 이메일로 변경합니다")
    @PostMapping("/change-email")
    public ResponseEntity<ApiResponse<Void>> changeEmail(@RequestBody @Validated ChangeEmailRequest request){
        userCommandService.changeEmail(request.getCurrentEmail(), request.getNewEmail());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "소셜 회원가입", description = "구글 로그인 이후 nickname 등 추가 정보를 받아 최종 가입을 완료합니다")
    @PostMapping("/signup-social")
    public ResponseEntity<ApiResponse<Void>> socialSignUp(@RequestBody @Validated GoogleSignUpRequest request){
        userCommandService.completeSocialSignup(request.getEmail(), request.getNickname(), request.getItNewsSubscription());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/dev-mbti")
    @Operation(summary = "개발자 성향 저장", description = "개발자 성향 테스트 결과를 사용자 정보에 저장합니다")
    public ResponseEntity<ApiResponse<Void>> saveDevMbti(
            @RequestBody @Validated SaveDevMbti request,
            @AuthenticationPrincipal User user) {

        Integer userId = Integer.valueOf(user.getUsername());
        userCommandService.saveDevMbti(userId, request.getDevMbti());

        return ResponseEntity.ok(ApiResponse.success(null));
    }

}
