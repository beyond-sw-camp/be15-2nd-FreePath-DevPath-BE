package com.freepath.devpath.board.vote.command.controller;

import com.freepath.devpath.board.vote.command.dto.request.VoteParticipateRequest;
import com.freepath.devpath.board.vote.command.exception.VoteParticipateFailedException;
import com.freepath.devpath.board.vote.command.service.VoteService;
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
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> participateVote(
            @RequestBody @Validated VoteParticipateRequest voteParticipateRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        int userId = Integer.parseInt(userDetails.getUsername());

        voteService.participateVote(voteParticipateRequest,userId);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @ExceptionHandler(VoteParticipateFailedException.class)
    public ResponseEntity<ApiResponse<Void>> handleVoteParticipateFailedException(VoteParticipateFailedException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }
}
