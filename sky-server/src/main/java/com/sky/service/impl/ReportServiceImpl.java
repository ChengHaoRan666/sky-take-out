package com.sky.service.impl;

import com.sky.dto.DataOverViewQueryDTO;
import com.sky.dto.GoodsSalesDTO;
import com.sky.mapper.OrderMapper;
import com.sky.service.ReportService;
import com.sky.vo.SalesTop10ReportVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
}
