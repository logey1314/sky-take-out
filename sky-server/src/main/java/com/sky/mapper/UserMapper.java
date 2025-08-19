package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Mapper
public interface UserMapper {
    @Select("select * from user where openid=#{openId}")
    User getByOpenid(String openId);

    void insert(User user);
    @Select("select * from user where id=#{userId}")
    User getUserById(Long userId);


    Integer getNewUserStatistics(LocalDateTime beginTime, LocalDateTime endTime);

    @Select("select COUNT(id) from user where id is not null ")
    Integer getUserTotalStatistics();

    /**
     * 根据动态条件统计用户数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
