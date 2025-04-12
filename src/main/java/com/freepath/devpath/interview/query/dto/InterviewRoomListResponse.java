package com.freepath.devpath.interview.query.dto;

import com.freepath.devpath.common.dto.Pagination;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class InterviewRoomListResponse {
    private List<InterviewRoomDto> interviewRooms;
    private Pagination pagination;
}
