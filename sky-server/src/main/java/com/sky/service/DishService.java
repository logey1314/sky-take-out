package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    /**
     * 新增菜品与口味
     * @param dishDTO
     */
    public void saveWithFlavor(DishDTO dishDTO);

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 菜品批量删除
     * @param ids
     * @return
     */
    void deleteBath(List<Long> ids);
    /**
     * 根据ID查询菜品
     * @param id
     * @return
     */
    DishVO getByIdWithFlavor(Long id);

    /**
     * 修改菜品
     * @param dishDTO
     * @return
     */
    void updateWithFlavor(DishDTO dishDTO);

    /**
     * 启停售菜品
     * @param status
     * @param id
     * @return
     */
    void startAndClose(String status, String id);

    /**
     * 根据分类查询菜品
     * @param categoryId
     * @return
     */
    List<DishVO> getDishByCatogeryId(String categoryId);

    /**
     * 根据分类查询菜品
     * @param categoryId
     * @return
     */
    List<Dish> getDishByCategoryId(String categoryId);
}
