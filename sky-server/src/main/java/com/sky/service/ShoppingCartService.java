package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
    /**
     * 购物车添加商品
     * @param shoppingCartDTO
     * @return
     */
    void add(ShoppingCartDTO shoppingCartDTO);

    /**
     * 查看购物车
     * @return
     */
    List<ShoppingCart> getShoppingCart();
    /**
     * 清空购物车
     * @return
     */
    void clean();
}
