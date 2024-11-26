package com.sky.service;

import com.sky.dto.DataOverViewQueryDTO;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;

/**
 * @Author: 程浩然
 * @Create: 2024/11/26 - 15:04
 * @Description: 统计报表Service层
 */
public interface ReportService {

    /**
     * 查找销量前十菜品
     *
     * @param dataOverViewQueryDTO 起始时间和结束时间
     * @return 菜品信息
     */
    SalesTop10ReportVO topTen(DataOverViewQueryDTO dataOverViewQueryDTO);

    /**
     * 订单统计
     *
     * @param dataOverViewQueryDTO 开始时间和结束时间
     * @return 订单统计信息
     */
    OrderReportVO ordersStatistics(DataOverViewQueryDTO dataOverViewQueryDTO);
}
