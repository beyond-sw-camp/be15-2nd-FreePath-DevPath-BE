package com.freepath.devpath.user.query.controller;

import com.freepath.devpath.common.dto.ApiResponse;
import com.freepath.devpath.user.query.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserQueryController {
    private final UserQueryService userQueryService;

    @PostMapping("/find-id")
    public ResponseEntity<ApiResponse<String>> findLoginId(@RequestBody Map<String, String> body){
        String email = body.get("email");
        String loginId = userQueryService.findLoginIdByEmail(email);

        return ResponseEntity.ok(ApiResponse.success(loginId));
    }
}
