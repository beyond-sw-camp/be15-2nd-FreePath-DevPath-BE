package com.freepath.devpath.interview.query.service;

import com.freepath.devpath.common.dto.Pagination;
import com.freepath.devpath.common.exception.ErrorCode;
import com.freepath.devpath.interview.query.dto.InterviewRoomDto;
import com.freepath.devpath.interview.query.dto.InterviewRoomListResponse;
import com.freepath.devpath.interview.query.exception.InterviewRoomQueryCreationException;
import com.freepath.devpath.interview.query.exception.InterviewRoomQueryNotFoundException;
import com.freepath.devpath.interview.query.mapper.InterviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewQueryAdminService {

    private final InterviewMapper interviewMapper;

    /* 모든 유저에 대한 면접방 목록 조회 */
    @Transactional(readOnly = true)
    public InterviewRoomListResponse getAllInterviewRooms(int page, int size) {
        int offset = (page - 1) * size;
        List<InterviewRoomDto> response = null;

        try{
            response = interviewMapper.selectAllInterviewRooms(page, size, offset);
        } catch(Exception e){
            throw new InterviewRoomQueryCreationException(ErrorCode.INTERVIEW_QUERY_CREATION_FAILED);
        }
        if (response == null || response.isEmpty()) {
            throw new InterviewRoomQueryNotFoundException(ErrorCode.INTERVIEW_ROOM_QUERY_NOT_FOUND);
        }

        // 페이징 처리
        int totalItems = interviewMapper.countAllInterviewRooms();
        Pagination pagination = Pagination.builder()
                .currentPage(page)
                .totalPage((int) Math.ceil((double) totalItems / size))
                .totalItems(totalItems)
                .build();

        return InterviewRoomListResponse.builder()
                .interviewRooms(response)
                .pagination(pagination)
                .totalInterviewRoomCount(totalItems)
                .build();
    }

    /* 특정 상태의 면접방 목록 조회*/
    public InterviewRoomListResponse getAllInterviewRoomsByStatus(String status, int page, int size) {
        int offset = (page - 1) * size;
        List<InterviewRoomDto> response;

        try {
            response = interviewMapper.selectAllInterviewRoomsByStatus(status, size, offset);
        } catch (Exception e) {
            throw new InterviewRoomQueryCreationException(ErrorCode.INTERVIEW_QUERY_CREATION_FAILED);
        }

        if (response == null || response.isEmpty()) {
            throw new InterviewRoomQueryNotFoundException(ErrorCode.INTERVIEW_ROOM_QUERY_NOT_FOUND);
        }

        // 페이징 처리
        int totalItems = interviewMapper.countAllInterviewRoomsByStatus(status);
        Pagination pagination = Pagination.builder()
                .currentPage(page)
                .totalPage((int) Math.ceil((double) totalItems / size))
                .totalItems(totalItems)
                .build();

        return InterviewRoomListResponse.builder()
                .interviewRooms(response)
                .pagination(pagination)
                .build();
    }


}
