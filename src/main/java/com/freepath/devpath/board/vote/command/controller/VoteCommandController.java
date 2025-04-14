package com.freepath.devpath.board.vote.command.controller;

import com.freepath.devpath.board.vote.command.dto.request.VoteParticipateRequest;
import com.freepath.devpath.board.vote.command.exception.VoteEndFailedException;
import com.freepath.devpath.board.vote.command.exception.VoteParticipateFailedException;
import com.freepath.devpath.board.vote.command.service.VoteCommandService;
import com.freepath.devpath.common.dto.ApiResponse;
import com.freepath.devpath.common.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vote")
@Tag(name = "투표 관리", description = "투표 참여 및 마감 기능 API")
public class VoteCommandController {

    private final VoteCommandService voteCommandService;

    @PostMapping
    @Operation(summary = "투표 참여", description = "투표 항목 ID를 선택하여 투표에 참여합니다.")
    public ResponseEntity<ApiResponse<Void>> participateVote(
            @Parameter(description = "투표 참여 요청 정보", required = true)
            @RequestBody @Validated VoteParticipateRequest voteParticipateRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        int userId = Integer.parseInt(userDetails.getUsername());
        voteCommandService.participateVote(voteParticipateRequest, userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("{vote-id}/end")
    @Operation(summary = "투표 마감", description = "투표를 마감합니다.")
    public ResponseEntity<ApiResponse<Void>> endVote(
            @Parameter(description = "마감할 투표 ID", required = true)
            @PathVariable("vote-id") int voteId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        int userId = Integer.parseInt(userDetails.getUsername());
        voteCommandService.endVote(voteId, userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @ExceptionHandler(VoteParticipateFailedException.class)
    public ResponseEntity<ApiResponse<Void>> handleVoteParticipateFailedException(VoteParticipateFailedException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(VoteEndFailedException.class)
    public ResponseEntity<ApiResponse<Void>> handleVoteEndFailedException(VoteEndFailedException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }
}
