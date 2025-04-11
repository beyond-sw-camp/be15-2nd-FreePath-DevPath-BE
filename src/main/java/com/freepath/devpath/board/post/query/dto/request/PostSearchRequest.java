package com.freepath.devpath.board.post.query.dto.request;

import com.freepath.devpath.board.post.query.exception.InvalidDateIntervalException;
import com.freepath.devpath.common.exception.ErrorCode;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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

    @Size(min = 2, message = "작성자 닉네임은 2글자 이상입니다.")
    private String nickname;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    // 최대 검색 기간 (4주 = 28일)
    private static final long MAX_DURATION_DAYS = 28;

    public boolean validateDateInterval() {
        if (startDate != null && endDate != null) {
            long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

            if (daysBetween > MAX_DURATION_DAYS) {
                return false;
            }
        }
        return true;
    }

    public int getOffset() {
        return (page - 1) * size;
    }

    public int getLimit() {
        return size;
    }
}
