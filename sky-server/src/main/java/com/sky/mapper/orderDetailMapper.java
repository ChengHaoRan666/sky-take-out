package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

    /**
     * 通过订单id查找订单菜品信息
     * @param orderId 订单id
     * @return 订单菜品list
     */
    @Select("select * from order_detail where order_id = #{orderId};")
    List<OrderDetail> detail(Long orderId);

    /**
     * 根据订单id删除订单菜品信息
     * @param orderId 订单id
     */
    @Delete("delete from order_detail where order_id = #{orderId};")
    void deleteByOrderId(Long orderId);
}
