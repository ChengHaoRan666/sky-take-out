package com.sky.mapper;

/**
 * @Author: 程浩然
 * @Create: 2024/11/16 - 14:58
 * @Description: 套餐Mapper接口
 */

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

}

