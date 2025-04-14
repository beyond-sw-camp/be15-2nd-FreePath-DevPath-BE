package com.freepath.devpath.board.vote.query.controller;


import com.freepath.devpath.board.post.command.exception.InvalidPostAuthorException;
import com.freepath.devpath.board.post.command.exception.NoSuchPostException;
import com.freepath.devpath.board.vote.query.dto.response.VoteDetailResponse;
import com.freepath.devpath.board.vote.query.service.VoteQueryService;
import com.freepath.devpath.common.dto.ApiResponse;
import com.freepath.devpath.common.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vote")
@Tag(name = "투표 조회 API", description = "투표 상세 조회 기능을 담당합니다.")
public class VoteQueryController {

    private final VoteQueryService voteQueryService;

    @GetMapping("/{board-id}")
    @Operation(summary = "투표 상세 조회", description = "게시글 ID를 기준으로 해당 게시글의 투표 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<VoteDetailResponse>> getVoteDetail(
            @Parameter(description = "투표 정보가 포함된 게시글 ID", required = true)
            @PathVariable("board-id") int boardId,

            @AuthenticationPrincipal UserDetails userDetails
    ) {
        int userId = Integer.parseInt(userDetails.getUsername());
        VoteDetailResponse response = voteQueryService.getVoteDetail(userId, boardId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @ExceptionHandler(NoSuchPostException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoSuchPostException(NoSuchPostException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }
}
