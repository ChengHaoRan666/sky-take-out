package com.sky.service;

import com.sky.dto.SetmealDTO;

/**
 * @Author: 程浩然
 * @Create: 2024/11/19 - 15:21
 * @Description: 套餐管理Service层
 */
public interface SetmealService {
    /**
     * 添加套餐和套餐菜品信息
     *
     * @param setmealDTO 套餐和套餐菜品信息
     */
    void addSetmeal(SetmealDTO setmealDTO);
}
