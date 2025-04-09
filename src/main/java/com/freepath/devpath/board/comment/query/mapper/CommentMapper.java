package com.freepath.devpath.board.comment.query.mapper;

import com.freepath.devpath.board.comment.query.dto.HierarchicalCommentDto;
import com.freepath.devpath.board.comment.query.dto.MyCommentResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {
    List<HierarchicalCommentDto> findHierarchicalComments(@Param("boardId") int boardId);
    List<MyCommentResponseDto> findMyComments(@Param("userId") int userId);
}