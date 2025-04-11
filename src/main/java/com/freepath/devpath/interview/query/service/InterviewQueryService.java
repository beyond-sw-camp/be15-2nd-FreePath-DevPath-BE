package com.freepath.devpath.interview.query.service;

import com.freepath.devpath.common.exception.ErrorCode;
import com.freepath.devpath.interview.query.dto.InterviewDetailDto;
import com.freepath.devpath.interview.query.dto.InterviewRoomDetailResponse;
import com.freepath.devpath.interview.query.dto.InterviewRoomDto;
import com.freepath.devpath.interview.query.dto.InterviewSummaryResponse;
import com.freepath.devpath.interview.query.exception.InterviewQueryAccessException;
import com.freepath.devpath.interview.query.exception.InterviewQueryHistoryNotFoundException;
import com.freepath.devpath.interview.query.exception.InterviewRoomQueryCreationException;
import com.freepath.devpath.interview.query.exception.InterviewRoomQueryNotFoundException;
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
        List<InterviewRoomDto> response = null;

        try{
            response = interviewMapper.selectInterviewRoomListByUserId(userId);
        } catch(Exception e){
            throw new InterviewRoomQueryCreationException(ErrorCode.INTERVIEW_QUERY_CREATION_FAILED);
        }

        if(response == null){
            throw new InterviewRoomQueryNotFoundException(ErrorCode.INTERVIEW_ROOM_QUERY_NOT_FOUND);
        }

        return response;
    }

    /* 특정 카테고리에 대한 면접방 목록만 조회 */
    @Transactional(readOnly = true)
    public List<InterviewRoomDto> getInterviewRoomListByCategory(Long userId, String category) {
        List<InterviewRoomDto> response;

        try {
            response = interviewMapper.selectInterviewRoomListByUserIdAndCategory(userId, category);
        } catch (Exception e) {
            throw new InterviewRoomQueryCreationException(ErrorCode.INTERVIEW_QUERY_CREATION_FAILED);
        }

        if (response == null || response.isEmpty()) {
            throw new InterviewRoomQueryNotFoundException(ErrorCode.INTERVIEW_ROOM_QUERY_NOT_FOUND);
        }

        return response;
    }

    /* 면접방 정보 및 면접 이력 조회 */
    @Transactional(readOnly = true)
    public InterviewRoomDetailResponse getInterviewRoomByRoomId(Long interviewRoomId, Long userId) {

        InterviewRoomDto room = interviewMapper.selectInterviewRoomByRoomId(interviewRoomId);

        // 유효한 면접방인지 검증
        if (room == null) {
            throw new InterviewRoomQueryNotFoundException(ErrorCode.INTERVIEW_ROOM_QUERY_NOT_FOUND);
        }

        // 면접방의 소유자 검증
        System.out.println("room.getUserId() : "+room.getUserId());
        System.out.println("userId : "+userId);
        if(room.getUserId() == null){
            throw new InterviewQueryAccessException(ErrorCode.INTERVIEW_ROOM_QUERY_NO_OWNER);
        }
        if(!room.getUserId().equals(userId)) {
            throw new InterviewQueryAccessException(ErrorCode.INTERVIEW_QUERY_ACCESS_DENIED);
        }

        // 면접방에 대한 면접 이력이 있는지 검증
        List<InterviewDetailDto> interviews = interviewMapper.selectInterviewListByRoomId(interviewRoomId);
        if (interviews == null || interviews.isEmpty()) {
            throw new InterviewQueryHistoryNotFoundException(ErrorCode.INTERVIEW_HISTORY_NOT_FOUND);
        }

        // 응답DTO 객체에 쿼리 결과값 저장
        InterviewRoomDetailResponse response = new InterviewRoomDetailResponse();
        try {
            response.setInterviewRoomId(room.getInterviewRoomId());
            response.setInterviewRoomTitle(room.getInterviewRoomTitle());
            response.setInterviewCategory(room.getInterviewCategory());
            response.setInterviewRoomMemo(room.getInterviewRoomMemo());
            response.setInterviewRomCreatedAt(room.getInterviewRoomCreatedAt());
            response.setInterviewList(interviews);
        } catch(Exception e){
            throw new InterviewRoomQueryCreationException(ErrorCode.INTERVIEW_QUERY_CREATION_FAILED);
        }

        return response;
    }

    /* 면접방의 총평 조회 */
    @Transactional(readOnly = true)
    public InterviewSummaryResponse getInterviewSummary(Long roomId, Long userId) {

        InterviewRoomDto room = interviewMapper.selectInterviewRoomByRoomId(roomId);

        // 유효한 면접방인지 검증
        if (room == null) {
            throw new InterviewRoomQueryNotFoundException(ErrorCode.INTERVIEW_ROOM_QUERY_NOT_FOUND);
        }

        // 면접방의 소유자 검증
        if (!room.getUserId().equals(userId)) {
            throw new InterviewQueryAccessException(ErrorCode.INTERVIEW_QUERY_ACCESS_DENIED);
        }

        // 면접방의 총평 생성
        String summary = interviewMapper.selectInterviewSummaryByRoomId(roomId);
        if (summary == null || summary.isEmpty()) {
            throw new InterviewQueryHistoryNotFoundException(ErrorCode.INTERVIEW_SUMMARY_NOT_FOUND);
        }

        // 응답
        return InterviewSummaryResponse.builder()
                .interviewRoomId(String.valueOf(roomId))
                .intervieRoomTitle(room.getInterviewRoomTitle())
                .summary(summary)
                .build();
    }

}