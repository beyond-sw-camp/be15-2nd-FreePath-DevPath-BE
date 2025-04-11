package com.freepath.devpath.report.service;

import com.freepath.devpath.board.comment.command.domain.repository.CommentRepository;
import com.freepath.devpath.board.post.command.repository.PostRepository;
import com.freepath.devpath.report.domain.Report;
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

    @Transactional
    public Report savePostReport(int userId, int boardId) {

        Report report = Report.builder()
                .reporterId(userId)
                .postId(boardId)
                .reportedAt(new Date())
                .build();

        long reportCount = reportRepository.countByPostId(boardId);
        if (reportCount >= 5) {
            postRepository.updateBoardStatusToReported(boardId);
        }

        return reportRepository.save(report);
    }

    @Transactional
    public Report saveCommentReport(int userId, int commentId) {

        Report report = Report.builder()
                .reporterId(userId)
                .commentId(commentId)
                .reportedAt(new Date())
                .build();

        long reportCount = reportRepository.countByCommentId(commentId);
        if (reportCount >= 5) {
            commentRepository.updateCommentStatusToReported(commentId);
        }

        return reportRepository.save(report);
    }
}
