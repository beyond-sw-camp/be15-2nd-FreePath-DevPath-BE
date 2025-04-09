package com.freepath.devpath.board.post.query.mapper;

import com.freepath.devpath.board.post.query.dto.request.PostSearchRequest;
import com.freepath.devpath.board.post.query.dto.response.CatetgoryDto;
import com.freepath.devpath.board.post.query.dto.response.PostDetailDto;
import com.freepath.devpath.board.post.query.dto.response.PostDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {
    PostDetailDto selectPostById(int boardId);

    List<PostDto> selectPostListByCategoryId(PostSearchRequest request);
    int countPostListByCategoryId(PostSearchRequest request);

    List<CatetgoryDto> selectCategoryListByParentCategoryId(int categoryId);
}
