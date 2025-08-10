package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface SetmealMapper {
    //TODO 为什么加了注解 公共字段没有填充
    @AutoFill(value = OperationType.INSERT)
    @Insert("insert into setmeal(category_id, name, price, description, image, status, create_time, update_time, create_user, update_user) " +
            "VALUES " +
            "(#{categoryId},#{name},#{price},#{description},#{image},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void addMeal(Setmeal setmeal);

    Page<Setmeal> page(SetmealPageQueryDTO setmealPageQueryDTO);

    @AutoFill(value = OperationType.UPDATE)
    @Update("update setmeal set category_id=#{categoryId}, name=#{name}, price=#{price}, " +
            "description=#{description}, image=#{image}, status=#{status}, " +
            "update_time=#{updateTime}, update_user=#{updateUser} where id=#{id}")
    void upadteMeal(Setmeal setmeal);

    @Update("update setmeal set status=#{status} where id=#{id}")
    void startAndClose(String status, String id);


    void deleteSetmeal(List<String> idslist);

    @Select("select  setmeal.status from setmeal where id=#{id}")
    String getStatus(String id);

    @Select("select * from setmeal where id=#{id}")
    Setmeal getSetmeal(String id);
}
