package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.controller.User.ShoppingCartController;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartlmpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        //判断当前商品是否以及加入购物车
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        Long currentId = BaseContext.getCurrentId();
        //shoppingCart.setId(currentId);
        shoppingCart.setUserId(currentId);

        List<ShoppingCart> shoppingCartList=shoppingCartMapper.list(shoppingCart);

        //如果存在，则数量加1
        if(shoppingCartList !=null&&shoppingCartList.size()>0){
            ShoppingCart cart = shoppingCartList.get(0);
            cart.setNumber(cart.getNumber()+1);
            shoppingCartMapper.addNumber(cart);
        }else {
            //是菜品还是套餐
            String name="";
            String image="";
            BigDecimal amount=new BigDecimal(0);
            Long dishId = shoppingCartDTO.getDishId();
            Long setmealId = shoppingCartDTO.getSetmealId();
            if(dishId!=null && setmealId==null){
                Dish dish = dishMapper.getById(dishId);
                name = dish.getName();
                image=dish.getImage();
                amount=dish.getPrice();
            } else if (dishId==null&&setmealId!=null) {
                Setmeal setmeal = setmealMapper.getSetmeal(String.valueOf(setmealId));
                name = setmeal.getName();
                image = setmeal.getImage();
                amount = setmeal.getPrice();
            }
            shoppingCart.setName(name);
            shoppingCart.setImage(image);
            shoppingCart.setNumber(1);
            shoppingCart.setAmount(amount);
            shoppingCart.setCreateTime(LocalDateTime.now());

            shoppingCartMapper.add(shoppingCart);

        }
        //不存在，插入购物车



    }
}
