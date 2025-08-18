package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.entity.User;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ReportMapper;
import com.sky.mapper.UserMapper;
import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

@Service
@Slf4j
public class ReportServicelmpl implements ReportService {

    @Autowired
    private ReportMapper reportMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 统计指定时间营业额
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        //存放日期范围
        ArrayList<LocalDate> dateList = new ArrayList<>();
        ArrayList<Object> turnOverList = new ArrayList<>();

        dateList.add(begin);
        while(!begin.equals(end)){
            begin=begin.plusDays(1);
            dateList.add(begin);
        }

        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Double over =orderMapper.getTurnOver(beginTime,endTime, Orders.COMPLETED);
            if(over==null){
                over=0.0;
            }
            turnOverList.add(over);
        }

        String s = StringUtils.join(dateList, ",");
        TurnoverReportVO reportVO = new TurnoverReportVO();

        String o = StringUtils.join(turnOverList, ",");

        reportVO.setTurnoverList(o);
        reportVO.setDateList(s);
        return reportVO;
    }

    /**
     * 统计指定时间用户数量
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        ArrayList<LocalDate> dateList = new ArrayList<>();
        ArrayList<Integer> newUserList = new ArrayList<>();
        ArrayList<Integer> totalUserList = new ArrayList<>();

        dateList.add(begin);
        while(!begin.equals(end)){
            begin=begin.plusDays(1);
            dateList.add(begin);
        }
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Integer newUser=userMapper.getNewUserStatistics(beginTime,endTime);
            if(newUser==null){
                newUser=0;
            }
            newUserList.add(newUser);

            Integer totalUser=userMapper.getUserTotalStatistics();
            if(totalUser==null){
                totalUser=0;
            }
            totalUserList.add(totalUser);
        }

        String date = StringUtils.join(dateList, ",");
        String ne = StringUtils.join(newUserList, ",");
        String total = StringUtils.join(totalUserList, ",");

        UserReportVO userReportVO = new UserReportVO();
        userReportVO.setDateList(date);
        userReportVO.setTotalUserList(total);
        userReportVO.setNewUserList(ne);

        return userReportVO;
    }
}
