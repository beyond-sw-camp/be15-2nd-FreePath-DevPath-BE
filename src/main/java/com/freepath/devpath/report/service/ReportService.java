package com.freepath.devpath.report.service;

import com.freepath.devpath.board.comment.command.domain.repository.CommentRepository;
import com.freepath.devpath.board.post.command.repository.PostRepository;
import com.freepath.devpath.report.domain.Report;
import com.freepath.devpath.report.domain.ReportCheck;
import com.freepath.devpath.report.repository.ReportCheckRepository;
import com.freepath.devpath.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ReportCheckRepository reportCheckRepository;

    @Transactional
    public Report savePostReport(int userId, int boardId) {

        Report report = Report.builder()
                .reporterId(userId)
                .postId(boardId)
                .reportedAt(new Date())
                .build();

        // 먼저 신고 테이블에 저장한 후 5회인지 count
        Report savedReport = reportRepository.save(report);
        long reportCount = reportRepository.countByPostId(boardId);

        // 딱 5번째일 때만 신고 검토 테이블에 추가
        if (reportCount == 5) {
            postRepository.updateBoardStatusToReported(boardId);    // is_board_deleted -> 'R'

            // 신고 검토 테이블에 추가 (해당 게시글에 대해 5번째로 신고된 신고 ID)
            ReportCheck reportCheck = ReportCheck.builder()
                    .reportId(savedReport.getReportId())
                    .build();

            reportCheckRepository.save(reportCheck);
        }

        return savedReport;
    }

    @Transactional
    public Report saveCommentReport(int userId, int commentId) {

        Report report = Report.builder()
                .reporterId(userId)
                .commentId(commentId)
                .reportedAt(new Date())
                .build();

        // 신고 먼저 저장
        Report savedReport = reportRepository.save(report);

        // 신고 건수 확인
        long reportCount = reportRepository.countByCommentId(commentId);

        if (reportCount == 5) { // 딱 5번째일 때만 처리
            commentRepository.updateCommentStatusToReported(commentId); // is_comment_deleted → 'R' 등 처리

            // 신고 검토 테이블에 추가 (5번째 신고만)
            ReportCheck reportCheck = ReportCheck.builder()
                    .reportId(savedReport.getReportId())
                    .build();

            reportCheckRepository.save(reportCheck);
        }

        return savedReport;
    }
}
