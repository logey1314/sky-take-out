package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 插入菜品数据
     * @param dish
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    @Select("select * from dish where id=#{id}")
    Dish getById(Long id);

    @Delete("delete from dish where id=#{id}")
    void deleteById(Long id);

    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);

    @Update("update dish set status=#{status} where id=#{id}")
    void startAndClose(String status, String id);



    @Select("select * from dish where category_id=#{categoryId}")
    ArrayList<Dish> getDishByCatogeryId(String categoryId);

    @Select("select * from dish where category_id=#{categoryId}")
    List<Dish> getDishByCategoryId(String categoryId);


//    @Select("select * from dish d left join  dish_flavor df on d.id=df.dish_id where d.category_id=#{categoryId}")
//    @Results(
//            {@Result(column = "value", property = "flavors")}
//    )
//    List<DishVO> add(String categoryId);
}
