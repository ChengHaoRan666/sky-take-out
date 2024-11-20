package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author: 程浩然
 * @Create: 2024/11/19 - 10:39
 * @Description:
 */
@Mapper
public interface SetmealDishMapper {
    /**
     * 根据菜品id查询对应的套餐id
     *
     * @param dishIds
     * @return
     */
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);

    /**
     * 添加套餐和菜品的关系
     *
     * @param setmealDishes 套餐和菜品关系
     */
    void addSetmealDish(List<SetmealDish> setmealDishes);

    /**
     * 根据套餐id查询套餐和菜品的关系
     * @param setmealId 套餐id
     * @return 套餐和菜品关系List
     */
    @Select("select * from setmeal_dish where setmeal_id = #{setmealId};")
    List<SetmealDish> getById(Long setmealId);

    /**
     * 删除套餐和菜品关系根据id
     * @param ids id的集合
     */
    void deleteBySetmealId(List<Long> ids);
}