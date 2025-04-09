package com.freepath.devpath.email.query.service;

import com.freepath.devpath.common.dto.Pagination;
import com.freepath.devpath.email.query.dto.NewsDto;
import com.freepath.devpath.email.query.dto.NewsListResponse;
import com.freepath.devpath.email.query.dto.NewsSearchRequest;
import com.freepath.devpath.email.query.mapper.NewsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsQueryService {

    private final NewsMapper newsMapper;

    public NewsDto getNews(int newsId) {
        return newsMapper.findNewsById(newsId);
    }

    @Transactional(readOnly = true)
    public NewsListResponse getNewsList(NewsSearchRequest newsSearchRequest) {

        List<NewsDto> news = newsMapper.selectNews(newsSearchRequest);
        long totalItems = newsMapper.countProducts(newsSearchRequest);

        int page = newsSearchRequest.getPage();
        int size = newsSearchRequest.getSize();

        return NewsListResponse.builder()
                .newsList(news)
                .pagination(Pagination.builder()
                        .currentPage(page)
                        .totalPage((int) Math.ceil((double) totalItems / size))
                        .totalItems(totalItems)
                        .build())
                .build();
    }
}
