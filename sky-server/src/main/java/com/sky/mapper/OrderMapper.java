package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: 程浩然
 * @Create: 2024/11/23 - 14:43
 * @Description: 用户订单表Mapper
 */

@Mapper
public interface OrderMapper {

    /**
     * 插入订单
     * @param order
     */
    void insert(Orders order);
}
