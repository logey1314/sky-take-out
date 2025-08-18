package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {


    void insert(Orders order);


    /**
     * 根据订单号和用户id查询订单
     * @param orderNumber
     * @param userId
     */
    @Select("select * from orders where number = #{orderNumber} and user_id= #{userId}")
    Orders getByNumberAndUserId(String orderNumber, Long userId);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);
    /**
     * 更新订单状态，处理支付和支付时间的更新
     *
     * @param orderStatus 订单状态
     * @param orderPaidStatus 订单支付状态
     */
    @Update("update orders set status = #{orderStatus}, pay_status = #{orderPaidStatus}, checkout_time = #{check_out_time} "
            + "where number = #{orderNumber}")
    void updateStatus(Integer orderStatus, Integer orderPaidStatus, LocalDateTime check_out_time, String orderNumber);

    @Select("SELECT * from orders where id=#{id}")
    Orders getOrder(String id);




    Page<Orders> getHistoryOrder(Long userId, String status);

    Page<OrderVO> getHistoryOrderByResultMap(Long userId, String status);

    @Update("update orders set status=#{status} ,pay_status=#{payStatus},cancel_time=#{cancelTime} where id=#{id}")
    void cancelOrder(Orders orders);

    /**
     * 管理端订单查询
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> orderSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    @Select("select  COUNT(*) from orders where status=#{status}")
    Integer countStatus(Integer status);

    @Update("update orders set status=3 where id=#{id}")
    void acceptOrder(OrdersConfirmDTO ordersConfirmDTO);

    @Select("select * from orders where status=#{status} and order_time<#{time}")
    List<Orders> getByStatusAndOrderTime(Integer pendingPayment, LocalDateTime time);



}
