package com.sky.mapper;

import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {


    void insert(Orders order);
}
