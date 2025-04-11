package com.freepath.devpath.board.vote.command.controller;

import com.freepath.devpath.board.vote.command.dto.request.VoteParticipateRequest;
import com.freepath.devpath.board.vote.command.exception.VoteEndFailedException;
import com.freepath.devpath.board.vote.command.exception.VoteParticipateFailedException;
import com.freepath.devpath.board.vote.command.service.VoteCommandService;
import com.freepath.devpath.common.dto.ApiResponse;
import com.freepath.devpath.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vote")
public class VoteCommandController {

    private final VoteCommandService voteCommandService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> participateVote(
            @RequestBody @Validated VoteParticipateRequest voteParticipateRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        int userId = Integer.parseInt(userDetails.getUsername());

        voteCommandService.participateVote(voteParticipateRequest,userId);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("{vote-id}/end")
    public ResponseEntity<ApiResponse<Void>> endVote(
            @PathVariable("vote-id") int voteId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        int userId = Integer.parseInt(userDetails.getUsername());

        voteCommandService.endVote(voteId,userId);

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
