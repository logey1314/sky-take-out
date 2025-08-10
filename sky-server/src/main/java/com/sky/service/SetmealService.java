package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    List<Setmeal> getSetmealByCategoryId(String categoryId);

    /**
     * 添加套餐
     * @param setmealDTO
     */
    void addMeal(SetmealDTO setmealDTO);

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult page(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 修改套餐
     * @param setmealDTO
     */
    void upadteMeal(SetmealDTO setmealDTO);

    /**
     *套餐起售与停售
     * @param status
     * @param id
     * @return
     */
    void startAndClose(String status, String id);

    /**
     * 批量删除套餐
     * @param ids
     * @return
     */
    void deleteSetmeal(String ids);

    SetmealVO getSetmeal(String id);
}
