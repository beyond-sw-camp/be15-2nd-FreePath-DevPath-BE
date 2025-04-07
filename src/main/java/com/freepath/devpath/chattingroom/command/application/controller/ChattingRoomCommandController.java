package com.freepath.devpath.chattingroom.command.application.controller;

import com.freepath.devpath.chattingroom.command.application.dto.response.ChattingRoomCommandResponse;
import com.freepath.devpath.chattingroom.command.application.service.ChattingRoomCommandService;
import com.freepath.devpath.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

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

    @PostMapping("/chatting/create/group/{boardId}")
    public ResponseEntity<ApiResponse<ChattingRoomCommandResponse>> createGroupChattingRoom(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable int boardId
    ){
        ChattingRoomCommandResponse chattingRoomCommandResponse =
                chattingRoomCommandService.createGroupChattingRoom(userDetails.getUsername(), boardId);
        return ResponseEntity.ok(ApiResponse.success(chattingRoomCommandResponse));
    }
}
