package com.sky.service;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {

    /**
     * 用户下单
     *
     * @param ordersSubmitDTO 订单数据
     * @return 订单提交结果
     */
    OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 用户支付
     *
     * @param ordersPaymentDTO 订单支付信息
     * @return 订单支付结果
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo 微信支付返回的订单号
     */
    void paySuccess(String outTradeNo);

    /**
     * 订单查询
     * @param ordersPageQueryDTO 订单查询条件
     * @return 订单列表结果
     */
    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 订单详情
     * @param orderId 订单id
     * @return 订单详情
     */
    OrderVO detail(Long orderId);
}
