package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderStatisticsVO;
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
    void cancel(Long orderId) throws Exception;

    /**
     * 分页查询
     *
     * @param page     当前页码
     * @param pageSize 每页数量
     * @param status   订单状态
     * @return 集合
     */
    PageResult pageQuery(int page, int pageSize, Integer status);

    /**
     * 再来一单
     *
     * @param id
     */
    void repetition(Long id);

    /**
     * 商家搜索订单
     *
     * @param ordersPageQueryDTO 搜索项
     * @return 订单信息
     */
    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 各个状态的订单数量统计
     *
     * @return 统计结果
     */
    OrderStatisticsVO statistics();

    /**
     * 接单，修改订单状态为已接单
     *
     * @param ordersConfirmDTO
     */
    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * 付款，修改订单状态为已付款
     *
     * @param orderNumber 订单号
     */
    void payment(String orderNumber);

    /**
     * 拒单
     *
     * @param ordersRejectionDTO 拒单信息
     */
    void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception;

    /**
     * 商家取消订单
     *
     * @param ordersCancelDTO 订单信息
     */
    void adminCancel(OrdersCancelDTO ordersCancelDTO) throws Exception;

    /**
     * 派送订单
     *
     * @param id 订单信息
     */
    void delivery(Long id);

    /**
     * 完成订单
     * @param id 订单id
     */
    void complete(Long id);

    /**
     * 催单
     * @param orderId
     */
    void reminder(Long orderId);
}