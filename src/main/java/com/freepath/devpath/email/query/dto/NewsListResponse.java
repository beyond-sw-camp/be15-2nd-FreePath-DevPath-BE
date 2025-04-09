package com.freepath.devpath.email.query.dto;

import com.freepath.devpath.common.dto.Pagination;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class NewsListResponse {
    private final List<NewsDto> newsList;
    private final Pagination pagination;
}