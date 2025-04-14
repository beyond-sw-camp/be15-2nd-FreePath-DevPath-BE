package com.freepath.devpath.report.repository;

import com.freepath.devpath.report.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {

    // 특정 게시글에 대해 사용자가 이미 신고했는지 확인
    boolean existsByReporterIdAndPostId(int reporterId, int postId);

    // 특정 댓글에 대해 사용자가 이미 신고했는지 확인
    boolean existsByReporterIdAndCommentId(int reporterId, int commentId);


    long countByPostId(Integer postId);

    long countByCommentId(Integer commentId);
}
