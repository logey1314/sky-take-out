package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.dto.SetmealDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface SetmealService {

    List<Setmeal> getSetmealByCategoryId(String categoryId);

    /**
     * 添加套餐
     * @param setmealDTO
     */
    void addMeal(SetmealDTO setmealDTO);
}
