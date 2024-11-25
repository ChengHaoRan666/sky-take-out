package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.orderDetailMapper;
import com.sky.mapper.shoppingCartMapper;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private WeChatPayUtil weChatPayUtil;

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
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder().orderTime(LocalDateTime.now()).orderNumber(order.getNumber()).orderAmount(order.getAmount()).build();

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
     * 订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
     * 要判断订单处于那个阶段，如果是待付款，待接单可以取消，之后的状态都需要联系商家取消
     * 如果在待接单状态还需要退款
     *
     * @param orderId 订单id
     */
    @Override
    @Transactional
    public void cancel(Long orderId) throws Exception {
        // 根据id查询订单
        Orders ordersDB = orderMapper.detail(orderId);

        // 校验订单是否存在
        if (ordersDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        //订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
        if (ordersDB.getStatus() > 2) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(ordersDB.getId());

        // 订单处于待接单状态下取消，需要进行退款
        if (ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            //调用微信支付退款接口
            weChatPayUtil.refund(ordersDB.getNumber(), //商户订单号
                    ordersDB.getNumber(), //商户退款单号
                    new BigDecimal(0.01),//退款金额，单位 元
                    new BigDecimal(0.01));//原订单金额

            //支付状态修改为 退款
            orders.setPayStatus(Orders.REFUND);
        }

        // 更新订单状态、取消原因、取消时间
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason("用户取消");
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    /**
     * 分页查询
     *
     * @param page     当前页码
     * @param pageSize 每页数量
     * @param status   订单状态
     * @return 集合
     */
    @Override
    public PageResult pageQuery(int page, int pageSize, Integer status) {
        // 设置分页
        PageHelper.startPage(page, pageSize);

        OrdersPageQueryDTO ordersPageQueryDTO = new OrdersPageQueryDTO();
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
        ordersPageQueryDTO.setStatus(status);

        // 分页条件查询
        Page<Orders> orderPage = orderMapper.pageQuery(ordersPageQueryDTO);

        List<OrderVO> list = new ArrayList();

        // 查询出订单明细，并封装入OrderVO进行响应
        if (orderPage != null && orderPage.getTotal() > 0) {
            for (Orders orders : orderPage) {
                Long orderId = orders.getId();// 订单id

                // 查询订单明细
                List<OrderDetail> orderDetails = orderDetailMapper.detail(orderId);

                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders, orderVO);
                orderVO.setOrderDetailList(orderDetails);

                list.add(orderVO);
            }
        }
        return new PageResult(orderPage.getTotal(), list);
    }

    /**
     * 再来一单
     *
     * @param id
     */
    @Override
    public void repetition(Long id) {
        // 查询当前用户id
        Long userId = BaseContext.getCurrentId();

        // 根据订单id查询当前订单详情
        List<OrderDetail> orderDetailList = orderDetailMapper.detail(id);

        // 将订单详情对象转换为购物车对象
        List<ShoppingCart> shoppingCartList = orderDetailList.stream().map(x -> {
            ShoppingCart shoppingCart = new ShoppingCart();

            // 将原订单详情里面的菜品信息重新复制到购物车对象中
            BeanUtils.copyProperties(x, shoppingCart, "id");
            shoppingCart.setUserId(userId);
            shoppingCart.setCreateTime(LocalDateTime.now());

            return shoppingCart;
        }).collect(Collectors.toList());

        // 将购物车对象批量添加到数据库
        shoppingCartMapper.insertBatch(shoppingCartList);
    }

    /**
     * 商家搜索订单
     *
     * @param ordersPageQueryDTO 搜索项
     * @return 订单信息
     */
    @Override
    @Transactional
    public PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        // 查找的有两项，一是订单的信息，二是订单中菜品的信息
        // 得到订单信息
        Page<Orders> orders = orderMapper.pageQuery(ordersPageQueryDTO);
        // 将订单order转为orderVO，将菜品信息也变为字符串加进去
        List<OrderVO> orderVOS = new ArrayList<>();

        for (Orders order : orders) {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(order, orderVO);
            String orderDishes = getOrderDishesStr(order);
            orderVO.setOrderDishes(orderDishes);
            orderVOS.add(orderVO);
        }

        return new PageResult(orders.getTotal(), orderVOS);
    }


    /**
     * 根据订单id获取菜品信息字符串
     *
     * @param orders 订单信息
     * @return 订单中菜品转换为的字符串
     */
    private String getOrderDishesStr(Orders orders) {
        // 查询订单菜品详情信息（订单中的菜品和数量）
        List<OrderDetail> orderDetailList = orderDetailMapper.detail(orders.getId());

        // 将每一条订单菜品信息拼接为字符串（格式：宫保鸡丁*3；）
        List<String> orderDishList = orderDetailList.stream().map(x -> {
            String orderDish = x.getName() + "*" + x.getNumber() + ";";
            return orderDish;
        }).collect(Collectors.toList());

        // 将该订单对应的所有菜品信息拼接在一起
        return String.join("", orderDishList);
    }

    /**
     * 各个状态的订单数量统计
     *
     * @return 统计结果
     */
    @Override
    public OrderStatisticsVO statistics() {
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        Integer statistic1 = orderMapper.statistics(Orders.TO_BE_CONFIRMED); // 待接单数量
        Integer statistic2 = orderMapper.statistics(Orders.CONFIRMED); // 待派送数量
        Integer statistic3 = orderMapper.statistics(Orders.DELIVERY_IN_PROGRESS); // 派送中数量
        orderStatisticsVO.setToBeConfirmed(statistic1);
        orderStatisticsVO.setConfirmed(statistic2);
        orderStatisticsVO.setDeliveryInProgress(statistic3);
        return orderStatisticsVO;
    }

    /**
     * 接单，修改订单状态为已接单
     *
     * @param ordersConfirmDTO
     */
    @Override
    public void confirm(OrdersConfirmDTO ordersConfirmDTO) {
        Orders orders = Orders.builder().id(ordersConfirmDTO.getId()).status(Orders.CONFIRMED).build();
        orderMapper.update(orders);
    }

    /**
     * 付款，修改订单状态为已付款
     *
     * @param orderNumber 订单号
     */
    @Override
    public void payment(String orderNumber) {
        Long userId = BaseContext.getCurrentId();
        Orders orders = orderMapper.getByNumberAndUserId(orderNumber, userId);
        orders.setStatus(Orders.REFUND);
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setPayStatus(Orders.PAID);
        orderMapper.update(orders);
    }

    /**
     * 拒单<br>
     * - 商家拒单其实就是将订单状态修改为“已取消”<br>
     * - 只有订单处于“待接单”状态时可以执行拒单操作<br>
     * - 商家拒单时需要指定拒单原因<br>
     * - 商家拒单时，如果用户已经完成了支付，需要为用户退款<br>
     *
     * @param ordersRejectionDTO 拒单信息
     */
    @Override
    public void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception {
        Orders orders = orderMapper.detail(ordersRejectionDTO.getId());

        // 订单只有存在且状态为2（待接单）才可以拒单
        if (orders == null || !orders.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        // 如果用户已经支付了
        if (orders.getPayStatus() == Orders.PAID) {
            //调用微信支付退款接口
            weChatPayUtil.refund(orders.getNumber(), //商户订单号
                    orders.getNumber(), //商户退款单号
                    new BigDecimal(0.01),//退款金额，单位 元
                    new BigDecimal(0.01));//原订单金额

            //支付状态修改为 退款
            orders.setPayStatus(Orders.REFUND);
        }
        orders.setStatus(Orders.CANCELLED);
        orders.setRejectionReason(ordersRejectionDTO.getRejectionReason());
        orders.setCancelTime(LocalDateTime.now());
        // 修改订单状态为已取消
        orderMapper.update(orders);
    }
}
