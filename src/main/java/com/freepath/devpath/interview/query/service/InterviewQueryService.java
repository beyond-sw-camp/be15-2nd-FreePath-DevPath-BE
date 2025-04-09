package com.freepath.devpath.interview.query.service;

import com.freepath.devpath.interview.query.dto.InterviewDetailDto;
import com.freepath.devpath.interview.query.dto.InterviewRoomDetailResponse;
import com.freepath.devpath.interview.query.dto.InterviewRoomDto;
import com.freepath.devpath.interview.query.mapper.InterviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewQueryService {

    private final InterviewMapper interviewMapper;

    /* 사용자가 진행한 면접방 목록 조회 */
    @Transactional(readOnly = true)
    public List<InterviewRoomDto> getInterviewRoomList(Long userId) {
        return interviewMapper.selectInterviewRoomListByUserId(userId);
    }

    /* 면접방 정보 및 면접 이력 조회 */
    @Transactional(readOnly = true)
    public InterviewRoomDetailResponse getInterviewRoomByRoomId(Long interviewRoomId) {

        InterviewRoomDetailResponse response = new InterviewRoomDetailResponse();

        InterviewRoomDto room = interviewMapper.selectInterviewRoomByRoomId(interviewRoomId);
        response.setInterviewRoomId(room.getInterviewRoomId());
        response.setInterviewCategory(room.getInterviewCategory());
        response.setInterviewRomCreatedAt(room.getInterviewRoomCreatedAt());

        List<InterviewDetailDto> interviews = interviewMapper.selectInterviewListByRoomId(interviewRoomId);
        response.setInterviewList(interviews);

        return response;
    }

}