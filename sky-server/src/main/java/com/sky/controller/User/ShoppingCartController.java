package com.sky.controller.User;


import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 购物车添加商品
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("购物车添加商品{}",shoppingCartDTO);
        shoppingCartService.add(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 查看购物车
     * @return
     */
    @GetMapping("/list")
    public Result<List<ShoppingCart>> getShoppingCart(){
        log.info("查看购物车");
        List<ShoppingCart> shoppingCartList=shoppingCartService.getShoppingCart();
        return Result.success(shoppingCartList);
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public Result delete(){
        log.info("清空购物车");
        shoppingCartService.clean();
        return Result.success();
    }

}
