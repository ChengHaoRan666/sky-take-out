package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: 程浩然
 * @Create: 2024/11/16 - 17:50
 * @Description: 菜品管理 Service 实现类
 */

@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    /**
     * 通过id查询菜品
     *
     * @param id 菜品id
     * @return 菜品信息DishVO
     */
    @Override
    public DishVO getById(Long id) {
        Dish dish = dishMapper.getById(id);
        List<DishFlavor> dishFlavors = dishFlavorMapper.getById(id);
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }

    /**
     * 添加菜品
     *
     * @param dishDTO 添加菜品的DTO
     */
    @Override
    public void addDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.addDish(dish);
        //获取insert语句生成的主键值
        Long dishId = dish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            //向口味表插入n条数据
            dishFlavorMapper.addDishFlavor(flavors);
        }
    }

    /**
     * 通过分类id查找菜品
     *
     * @param categoryId 分类id
     * @return 菜品集合
     */
    @Override
    public List<Dish> getByCategoryId(Long categoryId) {
        return dishMapper.getByCategoryId(categoryId);
    }

    /**
     * 分页查询
     *
     * @param dishPageQueryDTO 查询条件
     * @return
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }
}
