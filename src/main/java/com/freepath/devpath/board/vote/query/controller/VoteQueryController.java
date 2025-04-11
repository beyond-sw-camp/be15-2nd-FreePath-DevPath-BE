package com.freepath.devpath.board.vote.query.controller;


import com.freepath.devpath.board.vote.query.dto.response.VoteDetailResponse;
import com.freepath.devpath.board.vote.query.service.VoteQueryService;
import com.freepath.devpath.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vote")
public class VoteQueryController {

    private final VoteQueryService voteQueryService;

    @GetMapping("/{board-id}")
    public ResponseEntity<ApiResponse<VoteDetailResponse>> getVoteDetail(
            @PathVariable("board-id") int boardId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        int userId = Integer.parseInt(userDetails.getUsername());
        VoteDetailResponse response = voteQueryService.getVoteDetail(userId, boardId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
