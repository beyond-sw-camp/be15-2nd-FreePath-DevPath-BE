package com.freepath.devpath.email.query.controller;

import com.freepath.devpath.common.dto.ApiResponse;
import com.freepath.devpath.email.query.dto.NewsDto;
import com.freepath.devpath.email.query.dto.NewsListResponse;
import com.freepath.devpath.email.query.dto.NewsSearchRequest;
import com.freepath.devpath.email.query.service.NewsQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class NewsQueryController {

    private final NewsQueryService newsQueryService;

    @GetMapping("/news/{newsId}")
    public ResponseEntity<ApiResponse<NewsDto>> getNews(@PathVariable int newsId) {
        NewsDto response = newsQueryService.getNews(newsId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/news")
    public ResponseEntity<ApiResponse<NewsListResponse>> getNewsList(NewsSearchRequest newsSearchRequest){
        NewsListResponse response = newsQueryService.getNewsList(newsSearchRequest);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
