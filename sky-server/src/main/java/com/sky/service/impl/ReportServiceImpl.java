package com.sky.service.impl;

import com.sky.dto.DataOverViewQueryDTO;
import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import lombok.extern.slf4j.Slf4j;
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
    @Autowired
    private WorkspaceService workspaceService;

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


    /**
     * 导出30天数据
     *
     * @param response 响应
     */
    @Override
    public void export(HttpServletResponse response) {
        LocalDate begin = LocalDate.now().minusDays(30);
        LocalDate end = LocalDate.now().minusDays(1);
        //查询概览运营数据，提供给Excel模板文件
        BusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(begin, LocalTime.MIN), LocalDateTime.of(end, LocalTime.MAX));
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");
        try {
            //基于提供好的模板文件创建一个新的Excel表格对象
            XSSFWorkbook excel = new XSSFWorkbook(inputStream);
            //获得Excel文件中的一个Sheet页
            XSSFSheet sheet = excel.getSheet("Sheet1");

            sheet.getRow(1).getCell(1).setCellValue(begin + "至" + end);
            //获得第4行
            XSSFRow row = sheet.getRow(3);
            //获取单元格
            row.getCell(2).setCellValue(businessData.getTurnover());
            row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
            row.getCell(6).setCellValue(businessData.getNewUsers());
            row = sheet.getRow(4);
            row.getCell(2).setCellValue(businessData.getValidOrderCount());
            row.getCell(4).setCellValue(businessData.getUnitPrice());
            for (int i = 0; i < 30; i++) {
                LocalDate date = begin.plusDays(i);
                //准备明细数据
                businessData = workspaceService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));
                row = sheet.getRow(7 + i);
                row.getCell(1).setCellValue(date.toString());
                row.getCell(2).setCellValue(businessData.getTurnover());
                row.getCell(3).setCellValue(businessData.getValidOrderCount());
                row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
                row.getCell(5).setCellValue(businessData.getUnitPrice());
                row.getCell(6).setCellValue(businessData.getNewUsers());
            }
            //通过输出流将文件下载到客户端浏览器中
            ServletOutputStream out = response.getOutputStream();
            excel.write(out);
            //关闭资源
            out.flush();
            out.close();
            excel.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
