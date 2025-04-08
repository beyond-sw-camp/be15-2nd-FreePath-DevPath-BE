package com.freepath.devpath.csquiz.command.controller;

import com.freepath.devpath.common.response.ApiResponse;
import com.freepath.devpath.csquiz.command.dto.request.CsQuizResultRequest;
import com.freepath.devpath.csquiz.command.service.CsQuizResultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/csquiz")
@RequiredArgsConstructor
public class UserCsQuizController {

    private final CsQuizResultService resultService;

    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<Void>> submitAnswer(@RequestBody @Valid CsQuizResultRequest request) {
        resultService.submitAnswer(request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
