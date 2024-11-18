package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.vo.DishVO;

/**
 * @Author: 程浩然
 * @Create: 2024/11/16 - 17:50
 * @Description: 菜品管理 Service 层
 */

public interface DishService {

    /**
     * 通过id查询菜品
     *
     * @param id 菜品id
     * @return 菜品信息DishVO
     */
    DishVO getById(Long id);

    /**
     * 添加菜品
     *
     * @param dishDTO 添加菜品的DTO
     */
    void addDish(DishDTO dishDTO);
}
