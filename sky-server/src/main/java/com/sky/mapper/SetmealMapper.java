package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

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
}
