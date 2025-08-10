package com.sky.controller.User;

import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("UserSetmealController")
@RequestMapping("/user/setmeal")
@Api(tags = "套餐浏览接口")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * 用户端查据分类id查询套餐
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    public Result<List<Setmeal>> getSetmealByCategoryId(String categoryId) {
        log.info("用户端查据分类id查询套餐{}",categoryId);
        List<Setmeal> setmealList =setmealService.getSetmealByCategoryId(categoryId);
        return Result.success(setmealList);
    }

    /**
     * 客户端查询套餐菜品
     * @param setmealId
     * @return
     */
    @GetMapping("/dish/{id}")
    public Result<List<DishItemVO>> getDishBySetmealId(@PathVariable("id") String setmealId) {
        log.info("客户端查询套餐菜品{}",setmealId);
        List<DishItemVO> dishItemVOList= setmealService.getDishBySetmealId(setmealId);
        return Result.success(dishItemVOList);
    }



}
