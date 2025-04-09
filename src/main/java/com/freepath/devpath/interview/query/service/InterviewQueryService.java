package com.freepath.devpath.interview.query.service;

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

    @Transactional(readOnly = true)
    public List<InterviewRoomDto> getInterviewRoomList(Long userId) {
        return interviewMapper.selectInterviewRoomListByUserId(userId);
    }
}