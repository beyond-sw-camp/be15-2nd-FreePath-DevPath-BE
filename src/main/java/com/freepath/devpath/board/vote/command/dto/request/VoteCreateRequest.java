package com.freepath.devpath.board.vote.command.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class VoteCreateRequest {

    @NotBlank(message = "투표 제목은 필수입니다.")
    private String voteTitle;

    @NotNull(message = "투표 마감일은 필수입니다.")
    @Future(message = "투표 마감일은 현재보다 미래여야 합니다.")
    private LocalDateTime voteDuedate;

    @Size(min = 2, message = "투표 항목은 최소 2개 이상이어야 합니다.")
    private List<String> options;
}