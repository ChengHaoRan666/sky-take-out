package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: 程浩然
 * @Create: 2024/11/16 - 17:47
 * @Description: 菜品相关接口
 */

@Api(tags = "菜品相关接口")
@RestController("adminDishController")
@RequestMapping("/admin/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @GetMapping("/{id}")
    @ApiOperation("通过id查询菜品")
    public Result<DishVO> getById(@PathVariable("id") Long id) {
        DishVO dishVO = dishService.getById(id);
        return Result.success(dishVO);
    }


    @PostMapping
    @CacheEvict(cacheNames = "Dish", allEntries = true)
    @ApiOperation("新增菜品")
    public Result addDish(@RequestBody DishDTO dishDTO) {
        dishService.addDish(dishDTO);
        return Result.success();
    }

    /**
     * 根据分类id查找菜品
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查找菜品")
    public Result<List<Dish>> getByCategoryId(Long categoryId) {
        List<Dish> dishes = dishService.getByCategoryId(categoryId);
        return Result.success(dishes);
    }


    /**
     * 菜品分页查询
     */
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 修改菜品
     */
    @PutMapping
    @CacheEvict(cacheNames = "Dish", allEntries = true)
    @ApiOperation("修改菜品")
    public Result changeDish(@RequestBody DishDTO dishDTO) {
        dishService.changeDish(dishDTO);
        return Result.success();
    }

    /**
     * 批量删除菜品信息
     */
    @DeleteMapping
    @CacheEvict(cacheNames = "Dish", allEntries = true)
    @ApiOperation("批量删除菜品信息")
    public Result deleteDish(@RequestParam("ids") List<Long> ids) {
        dishService.deleteDish(ids);
        return Result.success();
    }

    /**
     * 菜品起售停售
     */
    @PostMapping("status/{status}")
    @CacheEvict(cacheNames = "Dish", allEntries = true)
    @ApiOperation("菜品起售停售")
    public Result status(@RequestParam("id") Long id, @PathVariable("status") Long status) {
        dishService.status(id, status);
        return Result.success();
    }
}
