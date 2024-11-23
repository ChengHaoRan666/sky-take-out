package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

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

    /**
     * 查询购物车中所有信息
     *
     * @return list
     */
    List<ShoppingCart> list();

    /**
     * 清空购物车
     */
    void clean();
}
