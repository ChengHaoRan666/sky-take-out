package com.sky.service.impl;

import com.sky.dto.DataOverViewQueryDTO;
import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: 程浩然
 * @Create: 2024/11/26 - 15:04
 * @Description: 统计报表Service层实现类
 */
@Slf4j
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 查找销量前十菜品
     *
     * @param dataOverViewQueryDTO 起始时间和结束时间
     * @return 菜品信息
     */
    @Override
    public SalesTop10ReportVO topTen(DataOverViewQueryDTO dataOverViewQueryDTO) {
        LocalDate begin = dataOverViewQueryDTO.getBegin();
        LocalDate end = dataOverViewQueryDTO.getEnd();
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        List<GoodsSalesDTO> goodsSalesDTOs = orderMapper.topTen(beginTime, endTime);

//        String nameList = "";
//        String numberList = "";
//        int i;
//        for (i = 0; i < goodsSalesDTOs.size() - 1; i++) {
//            nameList += goodsSalesDTOs.get(i).getName() + ",";
//            numberList += goodsSalesDTOs.get(i).getNumber() + ",";
//        }
//        nameList += goodsSalesDTOs.get(i).getName();
//        numberList += goodsSalesDTOs.get(i).getNumber();

        String nameList = goodsSalesDTOs.stream().map(GoodsSalesDTO::getName).collect(Collectors.joining(","));
        String numberList = goodsSalesDTOs.stream().map(GoodsSalesDTO::getNumber).map(Object::toString).collect(Collectors.joining(","));

        return SalesTop10ReportVO.builder()
                .nameList(nameList)
                .numberList(numberList)
                .build();
    }


    /**
     * 订单统计
     *
     * @param dataOverViewQueryDTO 开始时间和结束时间
     * @return 订单统计信息
     */
    @Override
    public OrderReportVO ordersStatistics(DataOverViewQueryDTO dataOverViewQueryDTO) {
        List<LocalDate> dataList = new ArrayList<>(); // 日期集合
        List<Integer> orderCountList = new ArrayList<>(); // 每日订单数集合
        List<Integer> validOrderCountList = new ArrayList<>();// 每日有效订单数集合
        Integer totalOrderCount = 0; // 订单总数
        Integer totalValidOrderCount = 0;// 有效订单总数
        Double orderCompletionRate = 0.0; // 订单完成率

        LocalDate begin = dataOverViewQueryDTO.getBegin();
        LocalDate end = dataOverViewQueryDTO.getEnd();

        while (!begin.isAfter(end)) {
            // 设置具体开始时间和结束时间为这一天
            LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(begin, LocalTime.MAX);
            // 查找这一天的全部订单
            Integer allOrderCount = orderMapper.ordersStatistics(beginTime, endTime, null);
            // 查找这一天的有效订单
            Integer validOrderCount = orderMapper.ordersStatistics(beginTime, endTime, Orders.COMPLETED);
            // 如果今天全部订单数不为空，就把今天的日期加到日期集合，订单数加到订单集合
            if (allOrderCount != 0) {
                dataList.add(begin);
                orderCountList.add(allOrderCount);
            }

            // 如果今天有效订单不为空，就将有效订单数加到有效订单数集合中
            if (validOrderCount != 0) {
                validOrderCountList.add(validOrderCount);
            }
            begin = begin.plusDays(1);
        }

        totalOrderCount = orderCountList.stream().mapToInt(Integer::intValue).sum();
        totalValidOrderCount = validOrderCountList.stream().mapToInt(Integer::intValue).sum();

        orderCompletionRate = totalOrderCount == 0 ? 0.0 : totalValidOrderCount.doubleValue() / totalOrderCount;


        // 将日期列表转换为字符串
        String dataString = dataList.stream().map(LocalDate::toString).collect(Collectors.joining(","));
        // 将每日订单数列表转换为字符串
        String orderCountString = orderCountList.stream().map(Object::toString).collect(Collectors.joining(","));
        // 将每日有效订单数列表转换为字符串
        String validOrderCountString = validOrderCountList.stream().map(Object::toString).collect(Collectors.joining(","));

        return OrderReportVO.builder()
                .dateList(dataString)
                .orderCountList(orderCountString)
                .validOrderCountList(validOrderCountString)
                .totalOrderCount(totalOrderCount)
                .validOrderCount(totalValidOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }


    /**
     * 营业额统计接口
     *
     * @param dataOverViewQueryDTO 开始时间和结束时间
     * @return 营业额统计信息
     */
    @Override
    public TurnoverReportVO turnoverStatistics(DataOverViewQueryDTO dataOverViewQueryDTO) {
        List<Double> turnoverList = new ArrayList<>();
        List<LocalDate> dataList = new ArrayList<>();
        LocalDate begin = dataOverViewQueryDTO.getBegin();
        LocalDate end = dataOverViewQueryDTO.getEnd();
        while (!begin.isAfter(end)) {
            LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(begin, LocalTime.MAX);
            Double turnover = orderMapper.getTurnover(beginTime, endTime);
            if (turnover != null) {
                turnoverList.add(turnover);
                dataList.add(begin);
            }
            begin = begin.plusDays(1);
        }

        // list转为String
        String dataString = dataList.stream().map(LocalDate::toString).collect(Collectors.joining(","));
        String turnoverString = turnoverList.stream().map(Object::toString).collect(Collectors.joining(","));

        return TurnoverReportVO
                .builder()
                .dateList(dataString)
                .turnoverList(turnoverString)
                .build();
    }

    /**
     * 用户统计接口
     *
     * @param dataOverViewQueryDTO 开始时间和结束时间
     * @return 用户统计信息
     */
    @Override
    public UserReportVO userStatistics(DataOverViewQueryDTO dataOverViewQueryDTO) {
        LocalDate begin = dataOverViewQueryDTO.getBegin();
        LocalDate end = dataOverViewQueryDTO.getEnd();
        LocalDateTime beginTime = null;
        LocalDateTime endTime = null;
        List<LocalDate> dataList = new ArrayList<>();
        List<Integer> totalUserList = new ArrayList<>();
        List<Integer> newUserList = new ArrayList<>();
        while (!begin.isAfter(end)) {
            beginTime = LocalDateTime.of(begin, LocalTime.MIN);
            endTime = LocalDateTime.of(begin, LocalTime.MAX);
            Integer newUser = userMapper.Statistics(beginTime, endTime);
            Integer totalUser = userMapper.Statistics(null, endTime);
            dataList.add(begin);
            totalUserList.add(totalUser);
            newUserList.add(newUser);
            begin = begin.plusDays(1);
        }

        String dataString = dataList.stream().map(LocalDate::toString).collect(Collectors.joining(","));
        String totalUserString = totalUserList.stream().map(Object::toString).collect(Collectors.joining(","));
        String newUserString = newUserList.stream().map(Object::toString).collect(Collectors.joining(","));


        return UserReportVO
                .builder()
                .dateList(dataString)
                .totalUserList(totalUserString)
                .newUserList(newUserString)
                .build();
    }
}
