package com.freepath.devpath.report.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportCheckRequest {
    private Integer postId;

    private Integer commentId;

    @NotNull(message = "검토 결과는 필수입니다.")
    private Character checkResult; // 'Y' or 'N'

    @NotBlank(message = "검토 사유는 필수입니다.")
    private String checkReason;

    public boolean isValidTarget() {
        return postId != null || commentId != null;
    }
}
