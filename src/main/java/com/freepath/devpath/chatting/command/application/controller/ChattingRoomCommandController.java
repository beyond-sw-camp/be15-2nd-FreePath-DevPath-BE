package com.freepath.devpath.chatting.command.application.controller;

import com.freepath.devpath.chatting.command.application.dto.request.GroupChattingRoomCreateRequest;
import com.freepath.devpath.chatting.command.application.dto.request.GroupChattingRoomUpdateRequest;
import com.freepath.devpath.chatting.command.application.dto.response.ChattingRoomCommandResponse;
import com.freepath.devpath.chatting.command.application.service.ChattingRoomCommandService;
import com.freepath.devpath.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ChattingRoomCommandController {
    private final ChattingRoomCommandService chattingRoomCommandService;

    @PostMapping("/chatting/create/{userId}")
    public ResponseEntity<ApiResponse<ChattingRoomCommandResponse>> createChattingRoom(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable int userId
    ){
        ChattingRoomCommandResponse chattingRoomCommandResponse =
                chattingRoomCommandService.createChattingRoom(userDetails.getUsername(), userId);
        return ResponseEntity.ok(ApiResponse.success(chattingRoomCommandResponse));
    }

    @PostMapping("/chatting/create/group")
    public ResponseEntity<ApiResponse<ChattingRoomCommandResponse>> createGroupChattingRoom(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody GroupChattingRoomCreateRequest request
            ){
        ChattingRoomCommandResponse chattingRoomCommandResponse =
                chattingRoomCommandService.createGroupChattingRoom(userDetails.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.success(chattingRoomCommandResponse));
    }

    @PutMapping("/chatting/update/group")
    public ResponseEntity<ApiResponse<Void>> updateGroupChattingRoom(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody GroupChattingRoomUpdateRequest request
    ){
        chattingRoomCommandService.updateChattingRoomTitle(userDetails.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }


    @PutMapping("/chatting/list/{chattingRoomId}")
    public ResponseEntity<ApiResponse<Void>> quitChattingRoom(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable int chattingRoomId
    ){
        chattingRoomCommandService.quitChattingRoom(userDetails.getUsername(),chattingRoomId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/chatting/list/{chattingRoomId}")
    public ResponseEntity<ApiResponse<Void>> deleteChattingRoom(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable int chattingRoomId
    ){
        chattingRoomCommandService.deleteChattingRoom(userDetails.getUsername(),chattingRoomId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

}
