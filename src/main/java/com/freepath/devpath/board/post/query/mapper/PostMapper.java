package com.freepath.devpath.board.post.query.mapper;

import com.freepath.devpath.board.post.query.dto.request.PostSearchRequest;
import com.freepath.devpath.board.post.query.dto.response.CatetgoryDto;
import com.freepath.devpath.board.post.query.dto.response.PostDetailDto;
import com.freepath.devpath.board.post.query.dto.response.PostDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PostMapper {
    PostDetailDto selectPostById(int boardId);

    // 게시물 내용을 제외한 필터 검색 (동적 쿼리)
    List<PostDto> selectPostListByFilter(PostSearchRequest request);

    // 필터 검색 시 전체 게시물 count 반환(페이징 처리를 위해)
    int countPostListByFilter(PostSearchRequest request);

    List<CatetgoryDto> selectCategoryListByParentCategoryId(int categoryId);

    List<PostDto> selectPostListByUserId(Map<String, Object> request);

    int countMyPostList(Map<String, Object> request);

    List<PostDto> findBoardByIds(List<Integer> boardIds);

}
