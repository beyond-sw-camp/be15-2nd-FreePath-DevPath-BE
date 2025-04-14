package com.freepath.devpath.report.mapper;

import com.freepath.devpath.report.dto.response.ReportCheckWithIdDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReportMapper {
    List<ReportCheckWithIdDto> selectAllReportChecksWithId();
}
