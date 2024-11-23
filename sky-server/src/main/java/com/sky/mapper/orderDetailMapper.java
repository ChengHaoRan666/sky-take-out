package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: 程浩然
 * @Create: 2024/11/23 - 17:11
 * @Description: 订单明细表mapper
 */
@Mapper
public interface orderDetailMapper {

    /**
     * 批量插入订单明细数据
     *
     * @param orderDetailList 订单明细数据
     */
    void insertBatch(List<OrderDetail> orderDetailList);
}
