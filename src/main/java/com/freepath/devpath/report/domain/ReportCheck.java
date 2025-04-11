package com.freepath.devpath.report.domain;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "REPORT_CHECK")
public class ReportCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reportCheckId;

    private int reportId;
    private int adminId;
    private Date checkedAt;
    private String check_result;

}
