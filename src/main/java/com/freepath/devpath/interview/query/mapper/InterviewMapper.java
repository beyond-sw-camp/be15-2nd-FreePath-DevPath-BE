package com.freepath.devpath.interview.query.mapper;

import com.freepath.devpath.interview.query.dto.InterviewDetailDto;
import com.freepath.devpath.interview.query.dto.InterviewRoomDetailResponse;
import com.freepath.devpath.interview.query.dto.InterviewRoomDto;
import com.freepath.devpath.interview.query.service.InterviewQueryAdminService;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InterviewMapper {

    /* InterviewQueryService */
    /* 사용자가 진행한 면접방 목록 조회 */
    List<InterviewRoomDto> selectInterviewRoomListByUserId(Long userId, int size, int offset);
    int countInterviewRoomListByUserId(Long userId);

    /* 특정 카테고리에 대한 면접방 목록만 조회 */
    List<InterviewRoomDto> selectInterviewRoomListByUserIdAndCategory(Long userId, String category, int size, int offset);
    int countInterviewRoomListByUserIdAndCategory(Long userId, String category);

    /* 면접방에 대한 정보 조회 */
    InterviewRoomDetailResponse selectInterviewRoomByRoomId(Long interviewRoomId);

    /* 면접방에 있는 면접 내역 조회 */
    List<InterviewDetailDto> selectInterviewListByRoomId(Long interviewRoomId);

    /* 면접방의 총평 조회 */
    String selectInterviewSummaryByRoomId(Long roomId);


    /* InterviewQueryAdminService */
    /* 모든 유저에 대한 면접방 목록 조회 */
    List<InterviewRoomDto> selectAllInterviewRooms(int page, int size, int offset);
    int countAllInterviewRooms();


}