package com.freepath.devpath.email.query.controller;

import com.freepath.devpath.common.dto.ApiResponse;
import com.freepath.devpath.email.query.dto.NewsDto;
import com.freepath.devpath.email.query.dto.NewsListResponse;
import com.freepath.devpath.email.query.dto.NewsSearchRequest;
import com.freepath.devpath.email.query.service.NewsQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Tag(name = "IT 뉴스 기사 조회", description = "관리자가 IT 뉴스를 조회 기능 API")
public class NewsQueryController {

    private final NewsQueryService newsQueryService;

    @GetMapping("/news/{newsId}")
    @Operation(summary = "IT 뉴스 기사 상세 조회", description = "관리자가 IT 뉴스를 상세 조회 합니다.")
    public ResponseEntity<ApiResponse<NewsDto>> getNews(@PathVariable int newsId) {
        NewsDto response = newsQueryService.getNews(newsId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/news")
    @Operation(summary = "IT 뉴스 기사 목록 조회", description = "관리자가 IT 뉴스의 목록을 조회 합니다.")
    public ResponseEntity<ApiResponse<NewsListResponse>> getNewsList(NewsSearchRequest newsSearchRequest){
        NewsListResponse response = newsQueryService.getNewsList(newsSearchRequest);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
