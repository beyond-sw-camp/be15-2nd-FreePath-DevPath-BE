package com.freepath.devpath.csquiz.common.controller;

import com.freepath.devpath.common.response.ApiResponse;
import com.freepath.devpath.csquiz.common.dto.CsQuizCreateRequest;
import com.freepath.devpath.csquiz.common.entity.CsQuiz;
import com.freepath.devpath.csquiz.common.service.CsQuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/csquiz")
@RequiredArgsConstructor
public class AdminCsQuizController {

    private final CsQuizService csQuizService;
    @PostMapping("/quiz-add")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody @Valid CsQuizCreateRequest request) {
        csQuizService.registerQuiz(request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }


}
