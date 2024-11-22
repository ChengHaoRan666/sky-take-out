package com.sky.controller.user;

import com.sky.entity.Setmeal;
import com.sky.mapper.SetmealMapper;
import com.sky.result.Result;
import com.sky.vo.DishItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: 程浩然
 * @Create: 2024/11/22 - 11:18
 * @Description: 套餐浏览接口
 */
@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@Api(tags = "C端-套餐浏览接口")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 根据分类id查询套餐
     *
     * @param categoryId 菜品分类id
     * @return 套餐list
     */
    @GetMapping("/list")
    @ApiOperation("根据菜品分类id查询套餐")
    public Result<List<Setmeal>> list(Long categoryId) {
        Setmeal setmeal = Setmeal.builder()
                .status(1)
                .categoryId(categoryId)
                .build();
        List<Setmeal> setmealList = setmealMapper.list(setmeal);
        return Result.success(setmealList);
    }


    /**
     * 根据套餐id查询包含的菜品列表
     *
     * @param id 套餐id
     * @return 菜品list
     */
    @GetMapping("/dish/{id}")
    @ApiOperation("根据套餐id查询包含的菜品列表")
    public Result<List<DishItemVO>> dishList(@PathVariable("id") Long id) {
        List<DishItemVO> dishItemVOS = setmealMapper.getDishItemBySetmealId(id);
        return Result.success(dishItemVOS);
    }
}
