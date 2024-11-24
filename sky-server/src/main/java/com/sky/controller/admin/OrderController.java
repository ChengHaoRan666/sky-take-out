package com.sky.controller.admin;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 程浩然
 * @Create: 2024/11/24 - 9:42
 * @Description: 商家端订单管理
 */
@RestController("adminOrderController")
@Slf4j
@Api(tags = "商家端订单管理")
@RequestMapping("/admin/order")
public class OrderController {

}
