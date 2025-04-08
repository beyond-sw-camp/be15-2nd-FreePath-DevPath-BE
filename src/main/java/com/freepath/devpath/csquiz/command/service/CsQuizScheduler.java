package com.freepath.devpath.csquiz.command.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CsQuizScheduler {

    private final CsQuizSchedulerService quizSchedulerService;

    @Scheduled(cron = "0 0 8 ? * MON")
    public void scheduledSubmitQuiz() {
        quizSchedulerService.submitWeeklyQuiz();
    }
}
