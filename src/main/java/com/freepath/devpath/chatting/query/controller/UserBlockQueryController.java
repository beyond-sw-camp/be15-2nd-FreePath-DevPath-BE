package com.freepath.devpath.chatting.query.controller;

import com.freepath.devpath.chatting.query.dto.response.UserBlockResponse;
import com.freepath.devpath.chatting.query.service.UserBlockQueryService;
import com.freepath.devpath.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "차단 목록 조회", description = "차단 조회 API")
public class UserBlockQueryController {
    private final UserBlockQueryService userBlockQueryService;
    @GetMapping("/chatting/block")
    public ResponseEntity<ApiResponse<UserBlockResponse>> getUserBlocked(
            @AuthenticationPrincipal UserDetails userDetails
    ){
        UserBlockResponse userBlockResponse = userBlockQueryService.getUserBlocked(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(userBlockResponse));
    }


}
