package com.sky.service;

import com.sky.dto.ShoppingCartDTO;

/**
 * @Author: 程浩然
 * @Create: 2024/11/23 - 8:38
 * @Description: 购物车Service层
 */
public interface shoppingCartService {

    /**
     * 添加到购物车
     *
     * @param shoppingCartDTO 待添加信息
     */
    void add(ShoppingCartDTO shoppingCartDTO);
}
