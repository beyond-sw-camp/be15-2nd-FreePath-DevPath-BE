package com.freepath.devpath.board.post.query.service;

import com.freepath.devpath.board.post.query.dto.request.MyPostRequest;
import com.freepath.devpath.board.post.query.dto.request.PostSearchRequest;
import com.freepath.devpath.board.post.query.dto.response.*;
import com.freepath.devpath.board.post.query.exception.InvalidDateIntervalException;
import com.freepath.devpath.board.post.query.exception.NoSuchPostException;
import com.freepath.devpath.board.post.query.mapper.PostMapper;
import com.freepath.devpath.common.dto.Pagination;
import com.freepath.devpath.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostQueryService {
    private final PostMapper postMapper;

    /* 게시글 상세 조회 */
    @Transactional(readOnly = true)
    public PostDetailResponse getPostById(int boardId) {

        PostDetailDto postDetailDto = Optional.ofNullable(postMapper.selectPostById(boardId))
                .orElseThrow(() -> new NoSuchPostException(ErrorCode.POST_NOT_FOUND));

        return PostDetailResponse.builder()
                .postDetailDto(postDetailDto)
                .build();
    }

    /* 카테고리와 검색어를 통한 게시글 리스트 조회 */
    @Transactional(readOnly = true)
    public PostListResponse getPostListByCategoryId(PostSearchRequest request) {
        if (!request.validateDateInterval()) {
            throw new InvalidDateIntervalException(ErrorCode.POST_SEARCH_FAILED);
        }

        List<PostDto> posts = postMapper.selectPostListByFilter(request);
        int totalItems = postMapper.countPostListByFilter(request);

        int page = request.getPage();
        int size = request.getSize();

        Pagination pagination = Pagination.builder()
                .currentPage(page)
                .totalPage((int) Math.ceil((double) totalItems/size))
                .totalItems(totalItems)
                .build();

        return PostListResponse.builder()
                .posts(posts)
                .pagination(pagination)
                .build();
    }

    /* 카테고리의 하위 카테고리 리스트 조회 */
    @Transactional(readOnly = true)
    public CategoryListResponse getCategoryList(int categoryId) {
        List<CatetgoryDto> categories = postMapper.selectCategoryListByParentCategoryId(categoryId);

        return CategoryListResponse.builder()
                .categories(categories)
                .build();
    }

    /* 자신이 작성한 게시물 목록 조회 */
    @Transactional(readOnly = true)
    public MyPostListResponse getPostByUserId(int userId, MyPostRequest request) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("limit", request.getLimit());
        params.put("offset", request.getOffset());

        List<PostDto> myPosts = postMapper.selectPostListByUserId(params);
        int totalItems = postMapper.countMyPostList(params);

        int page = request.getPage();
        int size = request.getSize();

        Pagination pagination = Pagination.builder()
                .currentPage(page)
                .totalPage((int) Math.ceil((double) totalItems / size))
                .totalItems(totalItems)
                .build();

        return MyPostListResponse.builder()
                .myPosts(myPosts)
                .pagination(pagination)
                .build();
    }


}
