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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("UserDishController")
@RequestMapping("/user/dish")
@Api(tags = "菜品接口")
@Slf4j

public class DishController {
    @Autowired
    DishService dishService;

    /**
     * 根据分类查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类查询菜品")
    public Result<List<DishVO>> getDishByCatogeryId(@RequestParam("categoryId") String categoryId) {
        log.info("客户查询分类菜品categoryId={}", categoryId);
        List<DishVO> list= dishService.getDishByCatogeryId(categoryId);
        return Result.success(list);
    }

}
