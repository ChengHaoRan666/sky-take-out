package com.sky.controller.user;


import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: 程浩然
 * @Create: 2024/11/23 - 14:41
 * @Description: 订单
 */

@RestController("userOrderController")
@RequestMapping("/user/order")
@Slf4j
@Api(tags = "C端-订单接口")
public class OrderController {

    // 微信支付得到预付单信息
    private String WECHAT_PAYMENT = "https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi";
    @Autowired
    private OrderService orderService;

    //    @ApiImplicitParam("催单")
//    @GetMapping("/reminder/{id}")
//    public Result reminder(@PathVariable("id") Long id) {
//
//        return Result.success();
//    }
//
//    @ApiOperation("再来一单")
//    @PostMapping("/repetition/{id}")
//    public Result repetition(@PathVariable("id") Long id) {
//
//        return Result.success();
//    }
//
//
//    @ApiOperation("历史订单查询")
//    @GetMapping("/historyOrders")
//    public void historyOrders() {
//
//    }
//
//    @ApiOperation("取消订单")
//    @PutMapping("/cancel/{id}")
//    public void cancel() {
//
//    }
//
//
//    @ApiOperation("查询订单详情")
//    @GetMapping("/orderDetail/{id}")
//    public void orderDetail() {
//
//    }
//
    @ApiOperation("用户下单")
    @PostMapping("/submit")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        OrderSubmitVO orderSubmitVO = orderService.submit(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }


    @ApiOperation("订单支付")
    @PutMapping("/payment")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = new OrderPaymentVO();
        return Result.success(orderPaymentVO);
    }
}