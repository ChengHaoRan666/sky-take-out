package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: 程浩然
 * @Create: 2024/11/20 - 18:59
 * @Description: 店铺相关接口
 */
@Api(tags = "店铺相关接口")
@RestController
@RequestMapping("/admin/shop")
@Slf4j
public class ShopController {
    private final String KEY = "SHOPPING_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/status")
    @ApiOperation("获取营业状态")
    public Result<Integer> getState() {
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        return Result.success(status);
    }


    @PutMapping("/{status}")
    @ApiOperation("设置营业状态")
    public Result setState(@PathVariable("status") Integer status) {
        // 在redis中设置营生状态
        redisTemplate.opsForValue().set(KEY, status);
        return Result.success();
    }
}
