package com.freepath.devpath.chatting.command.application.controller;

import com.freepath.devpath.chatting.command.application.dto.request.WaitingRoomActionRequest;
import com.freepath.devpath.chatting.command.application.service.WaitingRoomCommandService;
import com.freepath.devpath.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class WaitingRoomCommandController {
    private final WaitingRoomCommandService waitingRoomCommandService;
    @PostMapping("/chatting/waitingRoom/{chattingRoomId}")
    public ResponseEntity<ApiResponse<Void>> groupChattingJoin(
            @PathVariable int chattingRoomId,
            @AuthenticationPrincipal UserDetails userDetails
            ){
        waitingRoomCommandService.joinRequest(chattingRoomId, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("/chatting/waitingRoom/respond")
    public ResponseEntity<ApiResponse<Void>> groupChattingAccept(
            @RequestBody WaitingRoomActionRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ){
        waitingRoomCommandService.requestRespond(request, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
