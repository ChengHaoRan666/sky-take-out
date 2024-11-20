package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @Author: 程浩然
 * @Create: 2024/11/19 - 15:21
 * @Description: 套餐管理Service层实现类
 */
@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;

    /**
     * 添加套餐和套餐菜品信息
     *
     * @param setmealDTO 套餐和套餐菜品信息
     */
    @Override
    @Transactional
    public void addSetmeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealMapper.addSetmeal(setmeal);
        Long setmealId = setmeal.getId();
        for (SetmealDish setmealDish : setmealDishes)
            setmealDish.setSetmealId(setmealId);
        setmealDishMapper.addSetmealDish(setmealDishes);
    }

    /**
     * 根据id查询套餐
     *
     * @param id 套餐id
     * @return 套餐VO
     */
    @Override
    @Transactional
    public SetmealVO getById(Long id) {
        SetmealVO setmealVO = new SetmealVO();
        Setmeal setmeal = setmealMapper.getById(id);
        BeanUtils.copyProperties(setmeal, setmealVO);
        List<SetmealDish> setmealDishes = setmealDishMapper.getById(id);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    /**
     * 分页查询
     *
     * @param setmealPageQueryDTO 分页查询参数
     * @return 分页查询
     */
    @Override
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 修改套餐状态
     *
     * @param id     套餐id
     * @param status 状态
     */
    @Override
    @Transactional
    public void status(Long id, Integer status) {
        //起售套餐时，判断套餐内是否有停售菜品，有停售菜品提示"套餐内包含未启售菜品，无法启售"
        if (Objects.equals(status, StatusConstant.ENABLE)) {
            List<Dish> dishList = dishMapper.getBySetmealId(id);
            if (dishList != null && !dishList.isEmpty()) {
                dishList.forEach(dish -> {
                    if (StatusConstant.DISABLE.equals(dish.getStatus())) {
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                });
            }
        }
        LocalDateTime time = LocalDateTime.now();
        Long userId = BaseContext.getCurrentId();
        setmealMapper.status(id, status, time, userId);
    }

    /**
     * 通过ids删除套餐
     *
     * @param ids id的集合
     */
    @Override
    @Transactional
    public void deleteByIds(List<Long> ids) {
        ids.forEach(id -> {
            Setmeal setmeal = setmealMapper.getById(id);
            if (StatusConstant.ENABLE.equals(setmeal.getStatus())) {
                //起售中的套餐不能删除
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });

        //删除套餐表中的数据
        setmealMapper.deleteById(ids);
        //删除套餐菜品关系表中的数据
        setmealDishMapper.deleteBySetmealId(ids);
    }

    /**
     * 修改套餐
     *
     * @param setmealDTO 修改信息
     */
    @Override
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealMapper.update(setmeal);
        Long setmealId = setmeal.getId();
        for (SetmealDish setmealDish : setmealDishes) setmealDish.setSetmealId(setmealId);
        setmealDishMapper.deleteBySetmealId(Collections.singletonList(setmealId));
        setmealDishMapper.addSetmealDish(setmealDishes);
    }
}
