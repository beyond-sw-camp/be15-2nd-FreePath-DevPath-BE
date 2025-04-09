package com.freepath.devpath.board.comment.query.service;

import com.freepath.devpath.board.comment.query.dto.CommentTreeDto;
import com.freepath.devpath.board.comment.query.dto.HierarchicalCommentDto;
import com.freepath.devpath.board.comment.query.dto.MyCommentResponseDto;
import com.freepath.devpath.board.comment.query.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentQueryService {

    private final CommentMapper commentMapper;

    public List<CommentTreeDto> getCommentsAsTree(int boardId) {
        List<HierarchicalCommentDto> flatList = commentMapper.findHierarchicalComments(boardId);

        Map<Integer, CommentTreeDto> dtoMap = new HashMap<>();
        List<CommentTreeDto> result = new ArrayList<>();

        for (HierarchicalCommentDto flat : flatList) {
            CommentTreeDto node = CommentTreeDto.builder()
                    .commentId(flat.getCommentId())
                    .nickname(flat.getNickname())
                    .contents(flat.getContents())
                    .createdAt(flat.getCreatedAt())
                    .modifiedAt(flat.getModifiedAt())
                    .replies(new ArrayList<>())
                    .build();

            dtoMap.put(flat.getCommentId(), node);

            if (flat.getParentCommentId() == null) {
                result.add(node); // 최상위 댓글이면 리스트에 바로 추가
            } else {
                CommentTreeDto parent = dtoMap.get(flat.getParentCommentId());
                if (parent != null) {
                    parent.getReplies().add(node);
                }
            }
        }

        return result;
    }

    public List<MyCommentResponseDto> getMyComments(int userId) {
        return commentMapper.findMyComments(userId);
    }
}