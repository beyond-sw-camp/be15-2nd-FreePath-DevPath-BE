package com.freepath.devpath.chatting.query.controller;

import com.freepath.devpath.chatting.command.application.dto.response.ChattingListResponse;
import com.freepath.devpath.chatting.query.service.ChattingQueryService;
import com.freepath.devpath.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChattingQueryController {
    private final ChattingQueryService chattingQueryService;

    //@GetMapping("/chatting/list/{chattingRoomId}")
    public ResponseEntity<ApiResponse<ChattingListResponse>> loadChattingList(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable int chattingRoomId
            ){
        int userId = Integer.parseInt(userDetails.getUsername());
        ChattingListResponse chattingListResponse = chattingQueryService.getChattingList(userId, chattingRoomId);

        return ResponseEntity.ok(ApiResponse.success(chattingListResponse));
    }


}
