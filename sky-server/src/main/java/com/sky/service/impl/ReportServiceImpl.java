package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 统计给定时间区间营业额数据
     *
     * @param begin 时间区间开始时间
     * @param end   时间区间结束时间
     * @return 营业额数据
     */
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        // * 创建包含begin到end之间所有日期的List
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.isAfter(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        // * 创建包含每天营业额数据的List
        List<Double> turnoverList = new ArrayList<>();
        dateList.forEach(date -> {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map map = new HashMap();
            map.put("begin", beginTime);
            map.put("end", endTime);
            map.put("status", Orders.COMPLETED);
            turnoverList.add(orderMapper.sumByMap(map) == null ? 0.0 : orderMapper.sumByMap(map));
        });
        // * 构造返回结果
        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }

    /**
     * 统计用户数据
     *
     * @param begin 时间区间开始时间
     * @param end   时间区间结束时间
     * @return 用户数据
     */
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        // * 创建包含begin到end之间所有日期的List
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.isAfter(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        // * 创建包含每天用户的总数量的List
        List<Integer> totalUserList = new ArrayList<>();
        // * 创建包含每天新增用户的数量的List
        List<Integer> newUserList = new ArrayList<>();
        dateList.forEach(date -> {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map map = new HashMap();
            map.put("end", endTime);
            totalUserList.add(userMapper.countByMap(map));
            map.put("begin", beginTime);
            newUserList.add(userMapper.countByMap(map));
        });
        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .build();
    }

    /**
     * 统计给定时间区间的订单数据
     *
     * @param begin 时间区间开始时间
     * @param end   时间区间结束时间
     * @return 订单数据
     */
    public OrderReportVO getOrdersStatistics(LocalDate begin, LocalDate end) {
        // * 创建包含begin到end之间所有日期的列表
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.isAfter(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        // * 创建包含每天订单总数量的列表
        List<Integer> orderCountList = new ArrayList<>();
        // * 创建包含每天订单已完成数量的列表
        List<Integer> validOrderCountList = new ArrayList<>();
        dateList.forEach(date -> {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map map = new HashMap();
            map.put("begin", beginTime);
            map.put("end", endTime);
            orderCountList.add(orderMapper.countByMap(map));
            map.put("status", Orders.COMPLETED);
            validOrderCountList.add(orderMapper.countByMap(map));
        });
        // * 每天订单总数量的和
        Integer totalCount = orderCountList.stream().reduce(Integer::sum).get();
        // * 每天订单已完成数量的和
        Integer validCount = validOrderCountList.stream().reduce(Integer::sum).get();
        // * 订单完成率
        Double orderCompletionRate = validCount.doubleValue() / totalCount;
        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .orderCountList(StringUtils.join(orderCountList, ","))
                .validOrderCountList(StringUtils.join(validOrderCountList, ","))
                .totalOrderCount(totalCount)
                .validOrderCount(validCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }
}
