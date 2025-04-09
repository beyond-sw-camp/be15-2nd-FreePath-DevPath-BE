package com.freepath.devpath.interview.query.mapper;

import com.freepath.devpath.interview.query.dto.InterviewRoomDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InterviewMapper {

    List<InterviewRoomDto> selectInterviewRoomListByUserId(Long userId);

}