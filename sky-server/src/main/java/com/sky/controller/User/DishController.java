package com.sky.controller.User;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("UserDishController")
@RequestMapping("/user/dish")
@Api(tags = "菜品接口")
@Slf4j

public class DishController {
    @Autowired
    DishService dishService;
    @Autowired
    RedisTemplate redisTemplate;


    /**
     * 根据分类查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类查询菜品")
    public Result<List<DishVO>> getDishByCatogeryId(@RequestParam("categoryId") String categoryId) {
        //构造key dish_id
        String key = "dish_" + categoryId;
        //redis查询是否存在菜品数据
        List<DishVO> list= (List<DishVO>) redisTemplate.opsForValue().get(key);
        //存在就返回 不查数据库
        if (list != null&&list.size()>0) {
            return Result.success(list);
        }
        //不存在 查询数据库

        log.info("客户查询分类菜品categoryId={}", categoryId);
         list= dishService.getDishByCatogeryId(categoryId);
        //不存在 放在redis里面
        redisTemplate.opsForValue().set(key,list);
        return Result.success(list);
    }

}
