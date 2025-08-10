package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController("AdminSetMealController")
@RequestMapping("/admin/setmeal")
@Slf4j
@Api(tags="套餐相关接口")
public class SetMealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * 新增套餐
     * @param setmealDTO
     * @return
     */
    @PostMapping
    public Result addMeal(@RequestBody SetmealDTO setmealDTO){
        log.info("新增套餐{}",setmealDTO);
        setmealService.addMeal(setmealDTO);
        return  Result.success();
    }

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result page(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("套餐分页查询{}",setmealPageQueryDTO);
        PageResult pageResult= setmealService.page(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 修改套餐
     * @param setmealDTO
     * @return
     */
    @PutMapping
    public Result upadteMeal(@RequestBody SetmealDTO setmealDTO){
        log.info("修改套餐{}",setmealDTO);
        setmealService.upadteMeal(setmealDTO);
        return Result.success();
    }

    /**
     *套餐起售与停售
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    public Result startAndClose(@PathVariable String status,String id){
        log.info("套餐起售与停售{}，ID：{}",status,id);
        setmealService.startAndClose(status,id);
        return Result.success();
    }

    /**
     * 批量删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result deleteSetmeal(String ids){
        log.info("批量删除套餐{}",ids);
        setmealService.deleteSetmeal(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<SetmealVO> getSetmeal(String id){
        log.info("查询套餐");
        SetmealVO setmealVO= setmealService.getSetmeal(id);
        return Result.success(setmealVO);
    }


}
