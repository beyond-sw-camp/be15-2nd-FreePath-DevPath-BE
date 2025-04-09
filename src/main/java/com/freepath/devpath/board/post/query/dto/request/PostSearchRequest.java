package com.freepath.devpath.board.post.query.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostSearchRequest {
    @Min(value = 1, message = "페이지 번호는 1 이상이어야 합니다.")
    private Integer page = 1;
    @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
    private Integer size = 10;
    @Min(value = 0, message = "카테고리 ID는 0 이상이어야 합니다.")
    private int categoryId;
    @Size(min = 2, message = "검색어는 2자 이상 입력해주세요.")
    private String keyWord;

    public int getOffset() {
        return (page - 1) * size;
    }

    public int getLimit() {
        return size;
    }
}
