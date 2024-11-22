package com.sky.controller.user;

import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: 程浩然
 * @Create: 2024/11/22 - 11:18
 * @Description: 菜品浏览接口
 */
@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "C端-菜品浏览接口")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId 分类id
     * @return 菜品list
     */
    @ApiOperation("根据分类id查询菜品")
    @GetMapping("/list")
    public Result<List<DishVO>> list(Long categoryId) {
        // 优化：先查看redis里有没有，如果有，直接用，如果没有，再查询数据库
        String key = "dish_" + categoryId; // 规则：dish_分类id
        List<DishVO> dishVOs = (List<DishVO>) redisTemplate.opsForValue().get(key);
        // 如果存在缓存
        if (dishVOs != null && !dishVOs.isEmpty()) {
            return Result.success(dishVOs);
        }


        dishVOs = dishService.listWithFlavor(categoryId);

        // 如果不存在缓存
        redisTemplate.opsForValue().set(key, dishVOs);
        return Result.success(dishVOs);
    }
}
