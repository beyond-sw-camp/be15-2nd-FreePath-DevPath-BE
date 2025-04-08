package com.freepath.devpath.chatting.command.application.controller;

import com.freepath.devpath.chatting.command.application.service.UserBlockCommandService;
import com.freepath.devpath.common.response.ApiResponse;
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
public class UserBlockCommandController {
    private final UserBlockCommandService userBlockCommandService;
    @PostMapping("/chatting/block/{userId}")
    public ResponseEntity<ApiResponse<Void>> blockUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable int userId
            ){
        userBlockCommandService.blockUser(userDetails.getUsername(),userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/chatting/block/{userId}")
    public ResponseEntity<ApiResponse<Void>> unblockUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable int userId
    ){
        userBlockCommandService.unblockUser(userDetails.getUsername(),userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
