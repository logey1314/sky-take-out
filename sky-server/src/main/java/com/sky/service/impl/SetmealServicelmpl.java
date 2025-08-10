package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.service.SetmealService;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class SetmealServicelmpl implements SetmealService {


    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private SetmealMapper setmealMapper;


    @Override
    public List<Setmeal> getSetmealByCategoryId(String categoryId) {
        ArrayList<Setmeal> setmeals =setmealDishMapper.getSetmealByCategoryId(categoryId);
        return setmeals;
    }

    /**
     * 添加套餐
     * @param setmealDTO
     */
    @Override
    public void addMeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmeal.setStatus(StatusConstant.DISABLE);
        setmealMapper.addMeal(setmeal);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmeal.getId());
        }
        setmealDishMapper.addSetmealDish(setmealDishes);
    }

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<Setmeal> page= setmealMapper.page(setmealPageQueryDTO);
        long total = page.getTotal();
        List<Setmeal> result = page.getResult();
        return new PageResult(total,result);
    }

    /**
     * 修改套餐
     * @param setmealDTO
     */
    @Override
    public void upadteMeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmeal.setStatus(StatusConstant.DISABLE);
        setmealMapper.upadteMeal(setmeal);
    }

    /**
     *套餐起售与停售
     * @param status
     * @param id
     * @return
     */
    @Override
    //TODO 需要进行判断
    public void startAndClose(String status, String id) {
        setmealMapper.startAndClose(status,id);
    }
    /**
     * 批量删除套餐
     * @param ids
     * @return
     */

    @Override
    public void deleteSetmeal(String ids) {
        //套餐是否在售，在售不能删除
        String[] idList = ids.split(",");
        for (String id : idList) {
            String status=setmealMapper.getStatus(id);

            if (status.equals(StatusConstant.ENABLE.toString())) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_DISHABLEABLE_FAILED);
            }
        }
        List<String> idslist = Arrays.asList(ids.split(","));
        setmealMapper.deleteSetmeal(idslist);
    }

    @Override
    public SetmealVO getSetmeal(String id) {
        return null;
    }

}
