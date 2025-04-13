package com.freepath.devpath.chatting.command.application.controller;

import com.freepath.devpath.chatting.command.application.service.UserBlockCommandService;
import com.freepath.devpath.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "차단", description = "차단 API")
public class UserBlockCommandController {
    private final UserBlockCommandService userBlockCommandService;

    @Operation(summary = "회원 차단", description = "사용자 id를 이용하여 회원을 차단한다..")
    @PostMapping("/chatting/block/{userId}")
    public ResponseEntity<ApiResponse<Void>> blockUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable int userId
            ){
        userBlockCommandService.blockUser(userDetails.getUsername(),userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "회원 차단 취소", description = "사용자 id를 이용하여 회원을 차단을 취소한다.")
    @DeleteMapping("/chatting/block/{userId}")
    public ResponseEntity<ApiResponse<Void>> unblockUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable int userId
    ){
        userBlockCommandService.unblockUser(userDetails.getUsername(),userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
