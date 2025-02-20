package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: 程浩然
 * @Create: 2024/11/19 - 15:22
 * @Description: 套餐管理控制层
 */
@RestController("adminSetmealController")
@Slf4j
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐管理")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * 新增套餐
     */
    @PostMapping
    @CacheEvict(cacheNames = "SetmealCache", allEntries = true)
    @ApiOperation("新增套餐")
    public Result addSetmeal(@RequestBody SetmealDTO setmealDTO) {
        setmealService.addSetmeal(setmealDTO);
        return Result.success();
    }

    /**
     * 根据id查询套餐
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询套餐")
    public Result<SetmealVO> getById(@PathVariable("id") Long id) {
        SetmealVO setmealVO = setmealService.getById(id);
        return Result.success(setmealVO);
    }


    /**
     * 分页查询
     */
    @GetMapping("page")
    @ApiOperation("分页查询")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageResult pageResult = setmealService.page(setmealPageQueryDTO);
        return Result.success(pageResult);
    }


    /**
     * 套餐起售、停售
     */
    @PostMapping("/status/{status}")
    @CacheEvict(cacheNames = "SetmealCache", allEntries = true)
    @ApiOperation("套餐起售、停售")
    public Result status(@PathVariable("status") Integer status, @RequestParam("id") Long id) {
        setmealService.status(id, status);
        return Result.success();
    }

    /**
     * 批量删除套餐
     */
    @DeleteMapping
    @ApiOperation("批量删除套餐")
    @CacheEvict(cacheNames = "SetmealCache", allEntries = true)
    public Result deleteByIds(@RequestParam("ids") List<Long> ids) {
        setmealService.deleteByIds(ids);
        return Result.success();
    }

    /**
     * 修改套餐
     */
    @PutMapping
    @ApiOperation("修改套餐")
    @CacheEvict(cacheNames = "SetmealCache", allEntries = true)
    public Result update(@RequestBody SetmealDTO setmealDTO) {
        setmealService.update(setmealDTO);
        return Result.success();
    }
}
