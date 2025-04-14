package com.freepath.devpath.report.repository;

import com.freepath.devpath.report.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {

    long countByPostId(Integer postId);

    long countByCommentId(Integer commentId);
}
