package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WorkspaceService workspaceService;

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

    /**
     * 获取销量排名top10
     *
     * @param begin 时间区间开始
     * @param end   时间区间结束
     * @return 销量排名top10
     */
    public SalesTop10ReportVO getTop10(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        List<GoodsSalesDTO> list = orderMapper.getTop10(beginTime, endTime);
        String nameList = StringUtils.join(list.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList()), ",");
        String numberList = StringUtils.join(list.stream().map(s -> s.getNumber().toString()).collect(Collectors.toList()), ",");
        return SalesTop10ReportVO.builder()
                .nameList(nameList)
                .numberList(numberList)
                .build();
    }

    /**
     * 导出数据
     *
     * @param response HttpServletResponse
     */
    public void exportData(HttpServletResponse response) {
        // * 查询最近30天的营业数据
        LocalDate begin = LocalDate.now().minusDays(30);
        LocalDate end = LocalDate.now().minusDays(1);
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        BusinessDataVO businessDataVO = workspaceService.getBusinessData(beginTime, endTime);
        // * 将数据封装成Excel对象
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");
        try {
            // * 基于模板创建Excel对象
            XSSFWorkbook excel = new XSSFWorkbook(in);
            // * 获取第一个Sheet
            XSSFSheet sheet = excel.getSheet("Sheet1");
            sheet.getRow(1).getCell(1).setCellValue("时间：" + begin + "至" + end);
            XSSFRow row = sheet.getRow(3);
            row.getCell(2).setCellValue(businessDataVO.getTurnover());
            row.getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());
            row.getCell(6).setCellValue(businessDataVO.getNewUsers());
            row = sheet.getRow(4);
            row.getCell(2).setCellValue(businessDataVO.getValidOrderCount());
            row.getCell(4).setCellValue(businessDataVO.getUnitPrice());
            // * 获取30天营业额数据
            for (int i = 0; i < 30; i++) {
                LocalDate date = begin.plusDays(i);
                BusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));
                row = sheet.getRow(7 + i);
                row.getCell(1).setCellValue(date.toString());
                row.getCell(2).setCellValue(businessData.getTurnover());
                row.getCell(3).setCellValue(businessData.getValidOrderCount());
                row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
                row.getCell(5).setCellValue(businessData.getUnitPrice());
                row.getCell(6).setCellValue(businessData.getNewUsers());
            }
            // * 输出Excel到浏览器
            ServletOutputStream out = response.getOutputStream();
            excel.write(out);
            // * 关闭流
            out.close();
            excel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
