package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.orderDetailMapper;
import com.sky.mapper.shoppingCartMapper;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 程浩然
 * @Create: 2024/11/23 - 14:43
 * @Description: 用户订单操作实现类
 */

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private orderDetailMapper orderDetailMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private shoppingCartMapper shoppingCartMapper;

    /**
     * 用户下单
     *
     * @param ordersSubmitDTO 下单传递的参数
     * @return OrderSubmitVO
     */
    @Override
    @Transactional
    public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) {
        // 1. 判断地址簿是否为空，购物车是否为空
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = ShoppingCart.builder().userId(userId).build();
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.list(shoppingCart);
        if (shoppingCarts == null || shoppingCarts.isEmpty()) {
            throw new AddressBookBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }


        // 2. 向订单中插入 1 条数据
        Orders order = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, order); // 拷贝一下
        order.setStatus(Orders.PENDING_PAYMENT); // 设置订单状态
        order.setUserId(userId); // 设置下单用户Id
        order.setOrderTime(LocalDateTime.now()); // 设置下单时间
        order.setPayStatus(Orders.UN_PAID); // 支付状态
        order.setNumber(String.valueOf(System.currentTimeMillis())); // 订单号
        order.setPhone(addressBook.getPhone()); // 手机号
        order.setConsignee(addressBook.getConsignee()); // 收货人
        order.setAddress(addressBook.getDetail()); // 地址
        orderMapper.insert(order);
        Long orderId = order.getId();

        List<OrderDetail> orderDetailList = new ArrayList<>();
        // 3. 向订单明细表插入多条数据
        for (ShoppingCart cart : shoppingCarts) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(orderId); // 订单id
            orderDetailList.add(orderDetail);
        }
        orderDetailMapper.insertBatch(orderDetailList);

        // 4. 清空购物车数据
        shoppingCartMapper.clear(userId);
        // 5. 构造返回值返回
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .orderTime(LocalDateTime.now())
                .orderNumber(order.getNumber())
                .orderAmount(order.getAmount())
                .build();

        return orderSubmitVO;
    }

    /**
     * 通过订单id查询订单信息
     *
     * @param orderId 订单id
     * @return 订单信息
     */
    @Override
    @Transactional
    public OrderVO detail(Long orderId) {
        Orders order = orderMapper.detail(orderId);
        List<OrderDetail> orderDetailList = orderDetailMapper.detail(orderId);
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        orderVO.setOrderDetailList(orderDetailList);
        return orderVO;
    }

    /**
     * 根据订单id取消订单
     *
     * @param orderId 订单id
     */
    @Override
    @Transactional
    public void cancel(Long orderId) {
        orderMapper.deleteByOrderId(orderId);
        orderDetailMapper.deleteByOrderId(orderId);
    }
}
