package com.sky.service.impl;

import com.sky.mapper.shoppingCartMapper;
import com.sky.service.shoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: 程浩然
 * @Create: 2024/11/23 - 8:39
 * @Description: 购物车Service实现类
 */
@Service
@Slf4j
public class shoppingCartServiceImpl implements shoppingCartService {
    @Autowired
    private shoppingCartMapper shoppingCartMapper;
}
