package com.sky.mapper;

/**
 * @Author: 程浩然
 * @Create: 2024/11/16 - 14:58
 * @Description: 套餐Mapper接口
 */

import com.sky.annotaion.AutoFill;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     *
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 添加套餐
     *
     * @param setmeal 套餐
     */
    @AutoFill(OperationType.INSERT)
    void addSetmeal(Setmeal setmeal);

    /**
     * 根据id查询套餐
     *
     * @param id 套餐id
     * @return 套餐VO
     */
    @Select("select * from setmeal where id = #{id};")
    Setmeal getById(Long id);
}

