package com.sky.service;

import com.sky.vo.TurnoverReportVO;

import java.time.LocalDate;

public interface ReportService {
    /**
     * 统计给定时间区间营业额数据
     * @param begin 时间区间开始时间
     * @param end 时间区间结束时间
     * @return 营业额数据
     */
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);
}
