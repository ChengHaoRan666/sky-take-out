package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

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

    /**
     * 根据id查询套餐
     * @param id 套餐id
     * @return 套餐VO
     */
    SetmealVO getById(Long id);

    /**
     * 分页查询
     * @param setmealPageQueryDTO 分页查询参数
     * @return 分页
     */
    PageResult page(SetmealPageQueryDTO setmealPageQueryDTO);
}
