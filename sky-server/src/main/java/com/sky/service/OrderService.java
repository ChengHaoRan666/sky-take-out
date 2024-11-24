package com.sky.service;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

/**
 * @Author: 程浩然
 * @Create: 2024/11/23 - 14:42
 * @Description: 用户订单操作接口
 */
public interface OrderService {

    /**
     * 用户下单
     *
     * @param ordersSubmitDTO 下单传递的参数
     * @return OrderSubmitVO
     */
    OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 通过订单id查询订单信息
     *
     * @param orderId 订单id
     * @return 订单信息
     */
    OrderVO detail(Long orderId);

    /**
     * 根据订单id取消订单
     *
     * @param orderId 订单id
     */
    void cancel(Long orderId);
}