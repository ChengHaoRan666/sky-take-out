package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Author: 程浩然
 * @Create: 2024/11/23 - 14:43
 * @Description: 用户订单表Mapper
 */

@Mapper
public interface OrderMapper {

    /**
     * 插入订单
     *
     * @param order
     */
    void insert(Orders order);

    /**
     * 根据订单号和用户id查询订单
     *
     * @param orderNumber 订单号
     * @param userId      用户id
     */
    @Select("select * from orders where number = #{orderNumber} and user_id= #{userId}")
    Orders getByNumberAndUserId(String orderNumber, Long userId);

    /**
     * 修改订单信息
     *
     * @param orders 订单信息
     */
    void update(Orders orders);


    /**
     * 通过订单id获取订单信息
     * @param orderId 订单id
     * @return 订单信息
     */
    @Select("select * from orders where id = #{orderId};")
    Orders detail(Long orderId);

    /**
     * 根据订单id删除订单
     * @param orderId 订单id
     */
    @Delete("delete from orders where id = #{orderId};")
    void deleteByOrderId(Long orderId);
}
