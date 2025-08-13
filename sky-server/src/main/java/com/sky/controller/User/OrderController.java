package com.sky.controller.User;


import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("UserOrderController")
@RequestMapping("/user/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 提交订单
     * @param ordersSubmitDTO
     * @return
     */
    @PostMapping("submit")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("用户提交订单{}", ordersSubmitDTO);
        OrderSubmitVO vo= orderService.submit(ordersSubmitDTO);
        return Result.success(vo);
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }

    /**
     * 查看详细订单
     * @param id
     * @return
     */
    @GetMapping("/orderDetail/{id}")
    public Result<OrderVO> getOrderDetail(@PathVariable  String id){
        log.info("查看订单{}",id);
        OrderVO orderVO= orderService.getOrderDetail(id);
        return Result.success(orderVO);
    }

    /**
     * 获取历史订单
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    @PostMapping("/historyOrders")
    public Result<PageResult> getHistoryOrder(Integer page,Integer pageSize,String status){
        log.info("获取历史订单");
        PageResult pageResult= orderService.getHistoryOrder(page,pageSize,status);
        return  Result.success(pageResult);
    }

}
