package com.freepath.devpath.report.controller;

import com.freepath.devpath.common.dto.ApiResponse;
import com.freepath.devpath.common.exception.ErrorCode;
import com.freepath.devpath.report.domain.Report;
import com.freepath.devpath.report.dto.request.ReportCheckRequest;
import com.freepath.devpath.report.dto.response.ReportCheckListResponse;
import com.freepath.devpath.report.exception.AlreadyCheckedReportException;
import com.freepath.devpath.report.exception.NoSuchReportCheckException;
import com.freepath.devpath.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/board/{board-id}/report")
    public ResponseEntity<ApiResponse<Report>> createBoardReport(
            @PathVariable("board-id") int boardId,
            @AuthenticationPrincipal User userDetails){

        int userId = Integer.parseInt(userDetails.getUsername());

        Report report = reportService.savePostReport(userId, boardId);

        return ResponseEntity.ok(ApiResponse.success(report));
    }

    @PostMapping("/comment/{comment-id}/report")
    public ResponseEntity<ApiResponse<Report>> creteCommentReport(
            @PathVariable("comment-id") int commentId,
            @AuthenticationPrincipal User userDetails){

        int userId = Integer.parseInt(userDetails.getUsername());
        Report report = reportService.saveCommentReport(userId, commentId);

        return ResponseEntity.ok(ApiResponse.success(report));
    }

    @GetMapping("/report-check")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<ReportCheckListResponse>> getReportCheckList() {
        ReportCheckListResponse response = reportService.getReportCheckList();

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/reportCheck/{reportId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> processReportCheck(
            @PathVariable("reportId") int reportCheckId,
            @Validated @ModelAttribute ReportCheckRequest reportCheckRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        int adminId = Integer.parseInt(userDetails.getUsername());
        reportService.processReportCheck(reportCheckId, reportCheckRequest, adminId);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @ExceptionHandler(NoSuchReportCheckException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoSuchReportCheckException(NoSuchReportCheckException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(AlreadyCheckedReportException.class)
    public ResponseEntity<ApiResponse<Void>> handleAlreadyCheckedReportException(AlreadyCheckedReportException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }
}
