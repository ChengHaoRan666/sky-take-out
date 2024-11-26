package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

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
     *
     * @param orderId 订单id
     * @return 订单信息
     */
    @Select("select * from orders where id = #{orderId};")
    Orders detail(Long orderId);

    /**
     * 根据订单id删除订单
     *
     * @param orderId 订单id
     */
    @Delete("delete from orders where id = #{orderId};")
    void deleteByOrderId(Long orderId);

    /**
     * 分页查询
     *
     * @param page     当前页码
     * @param pageSize 每页数量
     * @param status   订单状态
     * @return
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据订单状态搜索该状态数量
     *
     * @param status 订单状态
     * @return 该状态订单数量
     */
    @Select("select COUNT(*) from orders where status = #{status};")
    Integer statistics(int status);


    /**
     * 根据状态和下单时间查询订单
     *
     * @param status
     * @param orderTime
     */
    @Select("select * from orders where status = #{status} and order_time < #{orderTime}")
    List<Orders> getByStatusAndOrdertimeLT(Integer status, LocalDateTime orderTime);

    /**
     * 通过订单id查找订单号
     *
     * @param orderId 订单id
     * @return 订单号
     */
    @Select("select number from orders where id = #{orderId};")
    String getByNumberById(Long orderId);


    /**
     * 根据开始时间和结束时间获取这段时间内的菜品销量前十，返回菜品和销量
     *
     * @param begin
     * @param end
     * @return
     */
    List<GoodsSalesDTO> topTen(LocalDateTime begin, LocalDateTime end);

    /**
     * 通过开始时间和结束时间获取订单统计接口
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    Integer ordersStatistics(
            @Param("beginTime") LocalDateTime beginTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("status") Integer status);
}
