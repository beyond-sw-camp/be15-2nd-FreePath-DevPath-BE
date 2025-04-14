package com.freepath.devpath.email.command.application.controller;

import com.freepath.devpath.common.dto.ApiResponse;
import com.freepath.devpath.email.command.application.Dto.NewsRequestDto;
import com.freepath.devpath.email.command.application.service.NewsService;
import com.freepath.devpath.email.command.domain.domain.News;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Tag(name = "IT 뉴스 기사", description = "관리자가 IT 뉴스를 요약 및 저장, 전송 기능 API")
public class NewsController {

    private final NewsService newsService;

    // 뉴스 저장
    @PostMapping("/news")
    @Operation(summary = "IT 뉴스 기사 저장", description = "관리자가 IT 뉴스의 정보를 요약하고 보낼 시각을 저장합니다.")
    public ResponseEntity<ApiResponse<News>> saveNews(@RequestBody NewsRequestDto dto) {
        News news = newsService.saveNews(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(news));
    }

    // 뉴스 직접 발송
    @PostMapping("/send/{newsId}")
    @Operation(summary = "IT 뉴스 기사 수동 전송", description = "관리자가 IT 뉴스를 구독자들에게 수동으로 전송합니다.")
    public ResponseEntity<ApiResponse<Void>> sendNews(@PathVariable int newsId) {
        newsService.sendNewsToSubscribers(newsId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // 메일 스케쥴링 테스트 코드
    @GetMapping("/test/sendNews")
    @Operation(summary = "이메일 스케쥴링 테스트 코드", description = "메일 스케쥴링 테스트 코드 입니다.")
    public String testSendNews() {
        newsService.sendNewsForToday();  // 스케줄링 메서드 수동 호출
        return "메일 전송 테스트 완료";
    }

    @PutMapping("/news/{newsId}")
    @Operation(summary = "IT 뉴스 기사 수정", description = "관리자가 IT 뉴스에 대한 정보 또는 발송 날짜를 수정 합니다.")
    public ResponseEntity<ApiResponse<Void>> updateNews(@PathVariable int newsId, @RequestBody NewsRequestDto dto) {
        newsService.updateNews(newsId, dto);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // 뉴스 삭제
    @DeleteMapping("/news/{newsId}")
    @Operation(summary = "IT 뉴스 기사 삭제", description = "관리자가 IT 뉴스에 대한 정보를 삭제 합니다.")
    public ResponseEntity<ApiResponse<Void>> deleteNews(@PathVariable int newsId) {
        newsService.deleteNews(newsId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
