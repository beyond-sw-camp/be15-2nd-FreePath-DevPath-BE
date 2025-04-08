package com.freepath.devpath.csquiz.command.controller;

import com.freepath.devpath.common.response.ApiResponse;
import com.freepath.devpath.csquiz.command.dto.request.CsQuizCreateRequest;
import com.freepath.devpath.csquiz.command.dto.request.CsQuizUpdateRequest;
import com.freepath.devpath.csquiz.command.service.CsQuizSchedulerService;
import com.freepath.devpath.csquiz.command.service.CsQuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/csquiz")
@RequiredArgsConstructor
public class AdminCsQuizController {

    private final CsQuizService csQuizService;
    private final CsQuizSchedulerService csQuizSchedulerService;
    @PostMapping("/quiz-add")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody @Valid CsQuizCreateRequest request) {
        csQuizService.registerQuiz(request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("/modify/{csquizId}")
    public ResponseEntity<ApiResponse<Void>> update(
            @PathVariable int csquizId,
            @RequestBody @Valid CsQuizUpdateRequest csQuizUpdateRequest
    ) {
        csQuizService.updateQuiz(csquizId, csQuizUpdateRequest);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
//    @GetMapping("/quiz/{csquizId}")
//    public ResponseEntity<ApiResponse<CsQuizResponse>> getQuiz(@PathVariable int csquizId) {
//        CsQuizResponse quiz = csQuizService.getQuizWithOptions(csquizId);
//        return ResponseEntity.ok(ApiResponse.success(quiz));
//    }

//    @GetMapping("/weekly")
//    public ResponseEntity<ApiResponse<List<CsQuizResponse>>> getWeeklyQuiz() {
//        List<CsQuizResponse> weeklyQuiz = csQuizSchedulerService.getWeeklyQuiz();
//        return ResponseEntity.ok(ApiResponse.success(weeklyQuiz));
//    }


    /* 퀴즈 버전 갱신 테스트 (원래 자동으로 매주 월요일 오전 8시에 갱신) */
    @PostMapping("/submit-weekly")
    public ResponseEntity<ApiResponse<Void>> submitWeeklyQuiz() {
        csQuizSchedulerService.submitWeeklyQuiz();
        return ResponseEntity.ok(ApiResponse.success(null));
    }



}
