package com.freepath.devpath.report.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "REPORT_CHECK")
public class ReportCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reportCheckId;

    private int reportId;
    private Integer adminId;    // 기본값 NULL로 설정될 수 있게 Integer로 둠
    private Date checkedAt;
    private Character checkResult;
    private String checkReason;

    public void processReportCheck(int adminId, Date checkedAt, char checkResult, String checkReason) {
        this.adminId = adminId;
        this.checkedAt = checkedAt;
        this.checkResult = checkResult;
        this.checkReason = checkReason;
    }
}
