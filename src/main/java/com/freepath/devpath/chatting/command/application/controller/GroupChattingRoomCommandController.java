package com.freepath.devpath.chatting.command.application.controller;

import com.freepath.devpath.chatting.command.application.dto.request.GroupChattingRoomJoinRequest;
import com.freepath.devpath.chatting.command.application.service.GroupChattingRoomCommandService;
import com.freepath.devpath.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequiredArgsConstructor
public class GroupChattingRoomCommandController {
    private final GroupChattingRoomCommandService groupChattingRoomCommandService;
    @PostMapping("/chatting/waitingRoom/{chattingRoomId}")
    public ResponseEntity<ApiResponse<Void>> groupChattingJoin(
            @PathVariable int chattingRoomId,
            @AuthenticationPrincipal UserDetails userDetails
            ){
        groupChattingRoomCommandService.joinRequest(chattingRoomId, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
