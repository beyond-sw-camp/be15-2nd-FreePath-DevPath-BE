package com.freepath.devpath.board.comment.query.mapper;

import com.freepath.devpath.board.comment.query.dto.CommentDetailDto;
import com.freepath.devpath.board.comment.query.dto.HierarchicalCommentDto;
import com.freepath.devpath.board.comment.query.dto.MyCommentResponseDto;
import com.freepath.devpath.board.comment.query.dto.MyCommentSearchRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {
    List<HierarchicalCommentDto> findHierarchicalComments(@Param("boardId") int boardId);

    List<MyCommentResponseDto> selectMyComments(MyCommentSearchRequest searchRequest);

    long countMyComments(MyCommentSearchRequest searchRequest);

    List<MyCommentResponseDto> selectReportedComments(MyCommentSearchRequest searchRequest);

    long countReportedComments(MyCommentSearchRequest searchRequest);

    CommentDetailDto selectCommentById(int commentId);
}