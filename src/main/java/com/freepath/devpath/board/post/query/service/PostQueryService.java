package com.freepath.devpath.board.post.query.service;

import com.freepath.devpath.board.post.command.domain.BoardDocument;
import com.freepath.devpath.board.post.command.repository.PostElasticRepository;
import com.freepath.devpath.board.post.query.dto.request.MyPostRequest;
import com.freepath.devpath.board.post.query.dto.request.PostContentSearchRequest;
import com.freepath.devpath.board.post.query.dto.request.PostSearchRequest;
import com.freepath.devpath.board.post.query.dto.response.*;
import com.freepath.devpath.board.post.query.exception.InvalidDateIntervalException;
import com.freepath.devpath.board.post.query.exception.NoSuchPostException;
import com.freepath.devpath.board.post.query.mapper.PostMapper;
import com.freepath.devpath.common.dto.Pagination;
import com.freepath.devpath.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostQueryService {
    private final PostMapper postMapper;
    private final PostElasticRepository postElasticRepository;

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
    public PostListResponse searchPostList(PostSearchRequest request) {
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

        return getMyPostListResponse(request, myPosts, totalItems);
    }

    /* 자신이 작성한 신고 게시물 목록 조회 */
    @Transactional(readOnly = true)
    public MyPostListResponse getReportedPostListByUserId(int userId, MyPostRequest request) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("limit", request.getLimit());
        params.put("offset", request.getOffset());

        List<PostDto> myPosts = postMapper.selectReportedPostListByUserId(params);
        int totalItems = postMapper.countReportedPostList(params);

        return getMyPostListResponse(request, myPosts, totalItems);
    }


    @Transactional(readOnly = true)
    public PostListResponse searchBoardContentByKeyword(PostContentSearchRequest request) {
        // Elasticsearch에서 boardContents에 키워드가 포함된 게시글을 검색하고 페이징 처리
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());
        Page<BoardDocument> boardDocumentsPage = postElasticRepository.findByBoardContentsContaining(request.getKeyWord(), pageable);
        int totalItems = (int) boardDocumentsPage.getTotalElements();

        List<Integer> boardIds = boardDocumentsPage.getContent().stream()
                .map(BoardDocument::getBoardId)
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        int page = request.getPage();
        int size = request.getSize();

        // 게시글 목록 조회
        List<PostDto> posts = new ArrayList<>();

        if (!boardIds.isEmpty()) {
            posts = postMapper.findBoardByIds(boardIds); // 여러 개의 boardId를 한 번에 조회하는 메서드 사용
        }

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

    /* 게시글 상세 조회 */
    @Transactional(readOnly = true)
    public PostDetailDto getReportedPostById(int boardId) {

        PostDetailDto postDetailDto = Optional.ofNullable(postMapper.selectReportedPostById(boardId))
                .orElseThrow(() -> new NoSuchPostException(ErrorCode.POST_NOT_FOUND));

        return postDetailDto;
    }

    private MyPostListResponse getMyPostListResponse(MyPostRequest request, List<PostDto> myPosts, int totalItems) {
        int page = request.getPage();
        int size = request.getSize();

        Pagination pagination = Pagination.builder()
                .currentPage(page)
                .totalPage((int) Math.ceil((double) totalItems/size))
                .totalItems(totalItems)
                .build();

        return MyPostListResponse.builder()
                .myPosts(myPosts)
                .pagination(pagination)
                .build();
    }
}
