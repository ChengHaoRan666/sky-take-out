package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author: 程浩然
 * @Create: 2024/11/18 - 8:12
 * @Description: 菜品口味表
 */
@Mapper
public interface DishFlavorMapper {

    /**
     * 通过菜品id查询口味信息
     */
    @Select("select * from dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> getById(Long dishId);
}
