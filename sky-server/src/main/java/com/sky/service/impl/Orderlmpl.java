package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.service.ShoppingCartService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class Orderlmpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private WeChatPayUtil weChatPayUtil;


    @Override
    public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) {
        //处理业务异常  收货地址为空  购物车为空
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        if(addressBook == null) {
            throw  new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        if(list == null || list.size() == 0) {
            throw  new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }


        //向订单表插入一条数据
        User user=userMapper.getUserById(userId);
        String address=addressBook.getProvinceName()+addressBook.getCityName()+addressBook.getDistrictName()+addressBook.getDetail();
        Orders order = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, order);

        order.setStatus(Orders.PENDING_PAYMENT);
        order.setUserId(userId);
        order.setOrderTime(LocalDateTime.now());
        order.setPayStatus(Orders.UN_PAID);
        order.setUserName(user.getName());
        order.setPhone(addressBook.getPhone());
        order.setAddress(address);
        order.setConsignee(addressBook.getConsignee());
        order.setNumber(String.valueOf(System.currentTimeMillis()));


        orderMapper.insert(order);

        //向订单明细表添加n条
        List<OrderDetail> orderDetailList=new ArrayList<>();
        for (ShoppingCart cart : list) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(order.getId());
            orderDetailList.add(orderDetail);
        }
        orderDetailMapper.insertBath(orderDetailList);

        //清空购物车
        shoppingCartMapper.clean(userId);;
        //封装vo
        OrderSubmitVO vo = OrderSubmitVO.builder()
                .id(order.getId())
                .orderTime(order.getOrderTime())
                .orderNumber(order.getNumber())
                .orderAmount(order.getAmount())
                .build();
        return vo;
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getUserById(userId);

//        //调用微信支付接口，生成预支付交易单
//        JSONObject jsonObject = weChatPayUtil.pay(
//                ordersPaymentDTO.getOrderNumber(), //商户订单号
//                new BigDecimal(0.01), //支付金额，单位 元
//                "苍穹外卖订单", //商品描述
//                user.getOpenid() //微信用户的openid
//        );
//
//        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
//            throw new OrderBusinessException("该订单已支付");
//        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "ORDERPAID");
        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

// // 处理数据并返回相应的状态代码
        Integer OrderPaidStatus = Orders.PAID; // 表示订单已支付
        Integer OrderStatus = Orders.TO_BE_CONFIRMED; // 订单待确认

// // 记录支付时间 checkout_time，并执行更新操作
        LocalDateTime check_out_time = LocalDateTime.now();

// 获取订单号
        String orderNumber = ordersPaymentDTO.getOrderNumber();

// 更新订单状态
        log.info("调用updateStatus。用于更新支付状态和支付时间的问题");
        orderMapper.updateStatus(OrderStatus, OrderPaidStatus, check_out_time, orderNumber);

        return vo;

    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();

        // 根据订单号查询当前用户的订单
        Orders ordersDB = orderMapper.getByNumberAndUserId(outTradeNo, userId);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);
    }

    /**
     * 查看详细订单
     * @param id
     * @return
     */
    @Override
    public OrderVO getOrderDetail(String id) {
        List<OrderDetail> orderDetailList=orderDetailMapper.getOrderDetail(id);
        Orders order=orderMapper.getOrder(id);
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        orderVO.setOrderDetailList(orderDetailList);
        return orderVO;
    }

    /**
     * 获取历史订单
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    //TODO  两种方法实现分页 查询 联表且含有list查询
//    @Override
//    public PageResult getHistoryOrder(Integer page, Integer pageSize, String status) {
//        PageHelper.startPage(page, pageSize);
//        Long userId = BaseContext.getCurrentId();
//        Page<Orders> ordersPage = orderMapper.getHistoryOrder(userId,status);
//        ArrayList<OrderVO> orderVoList = new ArrayList<>();
//        List<Orders> ordersList = ordersPage.getResult();
//        for (Orders orders : ordersList) {
//            OrderVO orderVO = new OrderVO();
//            BeanUtils.copyProperties(orders, orderVO);
//            List<OrderDetail> orderDetail = orderDetailMapper.getOrderDetail(orders.getId().toString());
//            orderVO.setOrderDetailList(orderDetail);
//            orderVoList.add(orderVO);
//        }
//        long total = ordersPage.getTotal();
//        PageResult pageResult = new PageResult();
//        pageResult.setTotal(total);
//        pageResult.setRecords(orderVoList);
//        return pageResult;
//    }
    @Override
    public PageResult getHistoryOrder(Integer page, Integer pageSize, String status){
        PageHelper.startPage(page, pageSize);
        Long userId = BaseContext.getCurrentId();
        Page<OrderVO> orderVOPage= orderMapper.getHistoryOrderByResultMap(userId,status);
        return new PageResult(orderVOPage.getTotal(), orderVOPage.getResult());
    }

    /**
     * 取消订单
     * @param id
     * @return
     */
    @Override
    public void cancelOrder(String id) {

        Orders cancleOrder = orderMapper.getOrder(id);
        if (cancleOrder == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        Orders orders = new Orders();
        orders.setId(cancleOrder.getId());
        if(cancleOrder.getStatus().equals(Orders.TO_BE_CONFIRMED)){
            orders.setPayStatus(Orders.REFUND);
        }
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason("用户取消");
        orders.setCancelTime(LocalDateTime.now());

        // 订单状态修改 支付状态修改 取消原因 取消时间 送达时间为null
            orderMapper.cancelOrder(orders);
        //退款
    }
    /**
     * 再来一单
     * @param id
     * @return
     */
    @Override
    public void againOrder(String id) {
        Long userId = BaseContext.getCurrentId();
        List<OrderDetail> orderDetail = orderDetailMapper.getOrderDetail(id);
        ArrayList<ShoppingCart> shoppingCarts = new ArrayList<>();
        for (OrderDetail orderDetail1 : orderDetail) {
            ShoppingCart shoppingCart = new ShoppingCart();
            BeanUtils.copyProperties(orderDetail1, shoppingCart,"id");
            shoppingCart.setUserId(userId);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCarts.add(shoppingCart);
            shoppingCartMapper.add(shoppingCart);
        }
        //        //添加订单
//        Orders order = orderMapper.getOrder(id);
//        List<OrderDetail> orderDetail = orderDetailMapper.getOrderDetail(id);
//
//        Orders againOrder = new Orders();
//        BeanUtils.copyProperties(order, againOrder);
//
//        againOrder.setId(null);
//        againOrder.setNumber(String.valueOf(System.currentTimeMillis()));
//        againOrder.setStatus(Orders.PENDING_PAYMENT);
//        againOrder.setOrderTime(LocalDateTime.now());
//        againOrder.setCheckoutTime(null);
//        againOrder.setPayStatus(Orders.UN_PAID);
//        againOrder.setEstimatedDeliveryTime(null);
//        orderMapper.insert(againOrder);
//        //添加商品细节
//        List<OrderDetail> detailList = new ArrayList<OrderDetail>();
//        for (OrderDetail detail : orderDetail) {
//            OrderDetail orderDetail1 = new OrderDetail();
//            BeanUtils.copyProperties(detail, orderDetail1);
//            orderDetail1.setId(null);
//            orderDetail1.setOrderId(againOrder.getId());
//            detailList.add(orderDetail1);
//        }
//        orderDetailMapper.insertBath(detailList);
//
//    }


    }

    /**
     * 管理端订单查询
     * @param ordersPageQueryDTO
     * @return
     */
    @Override
    public PageResult orderSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        int page = ordersPageQueryDTO.getPage();
        int pageSize = ordersPageQueryDTO.getPageSize();
        PageHelper.startPage(page, pageSize);
        Page<Orders> ordersPage=orderMapper.orderSearch(ordersPageQueryDTO);
        List<Orders> ordersList = ordersPage.getResult();
        //需要封装成ordervo
        ArrayList<OrderVO> orderVOS = new ArrayList<>();

        for (Orders orders : ordersList) {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(orders, orderVO);
            List<OrderDetail> orderDetail = orderDetailMapper.getOrderDetail(orders.getId().toString());
            String s = new String();
            for (OrderDetail detail : orderDetail) {
                if(detail.getDishFlavor()!=null) {
                    s = s + detail.getName() + detail.getDishFlavor() + "*" + detail.getNumber();
                }
                else {
                    s = s + detail.getName()  + "*" + detail.getNumber();
                }

            }
            orderVO.setOrderDishes(s);
            orderVO.setOrderDetailList(orderDetail);


            orderVOS.add(orderVO);
        }


        long total = ordersPage.getTotal();
        
        return new PageResult(total, orderVOS);
    }
    /**
     * 订单统计
     * @return
     */
    @Override
    public OrderStatisticsVO orderStatistics() {
        Integer toBeConfirmed = orderMapper.countStatus(Orders.TO_BE_CONFIRMED);
        Integer confirmed = orderMapper.countStatus(Orders.CONFIRMED);
        Integer deliveryInProgress = orderMapper.countStatus(Orders.DELIVERY_IN_PROGRESS);
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        orderStatisticsVO.setToBeConfirmed(toBeConfirmed);
        orderStatisticsVO.setConfirmed(confirmed);
        orderStatisticsVO.setDeliveryInProgress(deliveryInProgress);
        return  orderStatisticsVO;
    }

    /**
     * 接单
     * @param ordersConfirmDTO
     * @return
     */
    @Override
    public void acceptOrder(OrdersConfirmDTO ordersConfirmDTO) {

        orderMapper.acceptOrder(ordersConfirmDTO);

    }


}
