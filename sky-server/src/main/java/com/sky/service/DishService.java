package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

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

    /**
     * 通过分类id查找菜品
     *
     * @param categoryId 分类id
     * @return 菜品集合
     */
    List<Dish> getByCategoryId(Long categoryId);

    /**
     * 分页查询
     *
     * @param dishPageQueryDTO 查询条件
     * @return
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 修改菜品
     *
     * @param dishDTO 修改菜品的信息
     */
    void changeDish(DishDTO dishDTO);

    /**
     * 删除菜品
     *
     * @param ids 根据id批量删除菜品
     */
    void deleteDish(List<Long> ids);

    /**
     * 修改菜品状态
     * @param id 菜品id
     * @param status 菜品状态
     */
    void status(Long id, Long status);

    /**
     * 通过分类id查询菜品
     * @param categoryId 分类id
     * @return 菜品VO
     */
    List<DishVO> listWithFlavor(Long categoryId);
}
