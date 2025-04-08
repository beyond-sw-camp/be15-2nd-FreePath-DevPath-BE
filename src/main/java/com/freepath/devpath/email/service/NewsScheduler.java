package com.freepath.devpath.email.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NewsScheduler {

    private final NewsService newsService;

    // 매일 자정 실행 (00:00)
    @Scheduled(cron = "0 0 8 * * *")
    public void scheduleDailyNewsSend() {
        newsService.sendNewsForToday();
    }
}
