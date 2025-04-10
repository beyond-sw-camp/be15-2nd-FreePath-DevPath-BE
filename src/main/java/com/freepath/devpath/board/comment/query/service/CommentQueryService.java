package com.freepath.devpath.board.comment.query.service;

import com.freepath.devpath.board.comment.query.dto.*;
import com.freepath.devpath.board.comment.query.mapper.CommentMapper;
import com.freepath.devpath.common.dto.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentQueryService {

    private final CommentMapper commentMapper;

    @Transactional(readOnly = true)
    public List<CommentTreeDto> getCommentsAsTree(int boardId) {
        List<HierarchicalCommentDto> flatList = commentMapper.findHierarchicalComments(boardId);

        Map<Integer, CommentTreeDto> dtoMap = new HashMap<>();
        List<CommentTreeDto> result = new ArrayList<>();

        for (int i = 0; i < flatList.size(); i++) {
            HierarchicalCommentDto flat = flatList.get(i);

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

    @Transactional(readOnly = true)
    public MyCommentListResponse getMyComments(MyCommentSearchRequest searchRequest, int userId) {

        searchRequest.setUserId(userId);

        List<MyCommentResponseDto> comments = commentMapper.selectMyComments(searchRequest);

        long totalItems = commentMapper.countMyComments(searchRequest);

        int page = searchRequest.getPage();
        int size = searchRequest.getSize();

        return MyCommentListResponse.builder()
                .comments(comments)
                .pagination(Pagination.builder()
                        .currentPage(page)
                        .totalPage((int) Math.ceil((double) totalItems / size))
                        .totalItems(totalItems)
                        .build())
                .build();
    }


}