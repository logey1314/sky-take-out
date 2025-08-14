package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import org.springframework.stereotype.Service;


public interface OrderService {
    /**
     * 提交订单
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);
    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    /**
     * 查看详细订单
     * @param id
     * @return
     */
    OrderVO getOrderDetail(String id);

    /**
     * 获取历史订单
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    PageResult getHistoryOrder(Integer page, Integer pageSize, String status);

    /**
     * 取消订单
     * @param id
     * @return
     */
    void cancelOrder(String id);

    /**
     * 再来一单
     * @param id
     * @return
     */
    void againOrder(String id);

    /**
     * 管理端订单查询
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult orderSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 订单统计
     * @return
     */
    OrderStatisticsVO orderStatistics();

    /**
     * 接单
     * @param ordersConfirmDTO
     * @return
     */
    void acceptOrder(OrdersConfirmDTO ordersConfirmDTO);
    /**
     * 拒单
     * @param rejectionDTO
     * @return
     */
    void regection(OrdersRejectionDTO rejectionDTO);

    /**
     * 商户取消订单
     * @param cancelDTO
     */
    void cancelOrder(OrdersCancelDTO cancelDTO);
}
