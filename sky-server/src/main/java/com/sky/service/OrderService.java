package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
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

    /**
     * 订单状态统计
     * @return 订单状态统计结果
     */
    OrderStatisticsVO statistics();

    /**
     * 确认订单
      * @param ordersConfirmDTO 确认订单参数
     */
    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * 拒单
     * @param ordersRejectionDTO 拒单参数
     */
    void reject(OrdersRejectionDTO ordersRejectionDTO);

    /**
     * 取消订单
     * @param ordersCancelDTO 取消订单参数
     */
    void cancel(OrdersCancelDTO ordersCancelDTO);

    /**
     * 派送订单
     * @param id 订单id
     */
    void delivery(Long id);

    /**
     * 完成订单
     * @param id 订单id
     */
    void complete(Long id);
}
