package com.freepath.devpath.interview.query.service;

import com.freepath.devpath.common.exception.ErrorCode;
import com.freepath.devpath.interview.query.dto.InterviewRoomDto;
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
    public List<InterviewRoomDto> getAllInterviewRooms() {
        List<InterviewRoomDto> result = interviewMapper.selectAllInterviewRooms();
        if (result == null || result.isEmpty()) {
            throw new InterviewRoomQueryNotFoundException(ErrorCode.INTERVIEW_ROOM_QUERY_NOT_FOUND);
        }
        return result;
    }
}
