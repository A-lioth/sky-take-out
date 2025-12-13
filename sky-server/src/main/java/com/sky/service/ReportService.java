package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import java.time.LocalDate;

public interface ReportService {
    /**
     * 统计给定时间区间的营业额数据
     *
     * @param begin 时间区间开始时间
     * @param end   时间区间结束时间
     * @return 营业额数据
     */
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);

    /**
     * 统计给定时间区间的用户数据
     *
     * @param begin 时间区间开始时间
     * @param end   时间区间结束时间
     * @return 用户数据
     */
    UserReportVO getUserStatistics(LocalDate begin, LocalDate end);

    /**
     * 统计给定时间区间的订单数据
     *
     * @param begin 时间区间开始时间
     * @param end   时间区间结束时间
     * @return 订单数据
     */
    OrderReportVO getOrdersStatistics(LocalDate begin, LocalDate end);

    /**
     * 获取销量排名top10
     *
     * @param begin 时间区间开始时间
     * @param end   时间区间结束时间
     * @return 销量排名top10
     */
    SalesTop10ReportVO getTop10(LocalDate begin, LocalDate end);
}
