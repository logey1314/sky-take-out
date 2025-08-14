package com.sky.controller.admin;


import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("AdminOrderController")
@RequestMapping("/admin/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;
    /**
     * 管理端订单查询
     * @param ordersPageQueryDTO
     * @return
     */
    @GetMapping("/conditionSearch")
    public Result<PageResult> orderSearch(OrdersPageQueryDTO ordersPageQueryDTO){
        log.info("订单查询{}",ordersPageQueryDTO);
        PageResult pageResult=orderService.orderSearch(ordersPageQueryDTO);
        return  Result.success(pageResult);
    }

    /**
     * 订单统计
     * @return
     */
    @GetMapping("/statistics")
    public Result<OrderStatisticsVO> orderStatistics(){
        OrderStatisticsVO orderStatisticsVO= orderService.orderStatistics();
        log.info("订单统计");
        return Result.success(orderStatisticsVO);
    }

    /**
     * 管理端查看订单详情
     * @param orderId
     * @return
     */
    @GetMapping("/details/{id}")
    public Result<OrderVO> getOrderDetail(@PathVariable("id") String orderId){
        log.info("查询订单详情{}",orderId);
        OrderVO orderDetail = orderService.getOrderDetail(orderId);
        return Result.success(orderDetail);
    }

    /**
     * 接单
     * @param ordersConfirmDTO
     * @return
     */
    @PutMapping("/confirm")
    public Result acceptOrder(@RequestBody OrdersConfirmDTO ordersConfirmDTO){
        log.info("接单{}",ordersConfirmDTO);
        orderService.acceptOrder(ordersConfirmDTO);
        return Result.success();
    }

    /**
     * 拒单
     * @param rejectionDTO
     * @return
     */
    @PutMapping("/rejection")
    public Result refuseOrder(@RequestBody OrdersRejectionDTO rejectionDTO){
        log.info("拒单{}",rejectionDTO);
        orderService.regection(rejectionDTO);
        return Result.success();
    }

    /**
     * 商户取消订单
     * @param cancelDTO
     * @return
     */
    @PutMapping("/cancel")
    public Result cancleOrder(@RequestBody OrdersCancelDTO cancelDTO){
        log.info("商户取消订单{}",cancelDTO);
        orderService.cancelOrder(cancelDTO);
        return Result.success();
    }






}
