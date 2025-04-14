package com.freepath.devpath.board.post.query.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CategoryListResponse {
    private final List<CatetgoryDto> categories;
}
