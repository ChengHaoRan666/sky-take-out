package com.sky.controller.user;

import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: 程浩然
 * @Create: 2024/11/22 - 11:18
 * @Description: 分类接口
 */
@RestController("userCategoryController")
@RequestMapping("/user/category")
@Api(tags = "C端-分类接口")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 查询分类类型
     *
     * @param type 分类类型：1 菜品分类 2 套餐分类
     * @return 分类list
     */
    @ApiOperation("条件查询")
    @GetMapping("/list")
    public Result<List<Category>> list(Integer type) {
        List<Category> categories = categoryMapper.list(type);
        return Result.success(categories);
    }
}
