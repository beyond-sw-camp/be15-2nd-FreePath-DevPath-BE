package com.freepath.devpath.report.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ReportCheckWithIdDto {
    private int reportCheckId;
    private int reportId;
    private Integer adminId;
    private Date checkedAt;
    private String checkResult;
    private String checkReason;
    private Integer postId;
    private Integer commentId;
}