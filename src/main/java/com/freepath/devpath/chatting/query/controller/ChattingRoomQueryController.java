package com.freepath.devpath.chatting.query.controller;

import com.freepath.devpath.chatting.query.dto.response.GroupChattingWaitingRoomResponse;
import com.freepath.devpath.chatting.query.dto.response.ChattingRoomResponse;
import com.freepath.devpath.chatting.query.service.ChattingRoomQueryService;
import com.freepath.devpath.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChattingRoomQueryController {
    private final ChattingRoomQueryService chattingRoomQueryService;

    @GetMapping("/chatting/list")
    public ResponseEntity<ApiResponse<ChattingRoomResponse>> getChattingRooms(
            @AuthenticationPrincipal UserDetails userDetails
    ){
        ChattingRoomResponse response = chattingRoomQueryService.getChattingRooms(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/chatting/waitingRoom/{chattingRoomId}")
    public ResponseEntity<ApiResponse<GroupChattingWaitingRoomResponse>> getWaitingRoom(
            @PathVariable int chattingRoomId,
            @AuthenticationPrincipal UserDetails userDetails
    ){
        GroupChattingWaitingRoomResponse groupChattingWaitingRoomResponse =
                chattingRoomQueryService.getWaitingRoom(chattingRoomId, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(groupChattingWaitingRoomResponse));
    }
}
