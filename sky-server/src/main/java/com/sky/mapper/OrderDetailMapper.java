package com.sky.mapper;

import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderDetailMapper {


    void insertBath(List<OrderDetail> orderDetailList);

    @Select("select * from order_detail where order_id=#{id}")
    List<OrderDetail> getOrderDetail(String id);
}
