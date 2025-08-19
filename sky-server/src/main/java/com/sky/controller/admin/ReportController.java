package com.sky.controller.admin;


import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

@RestController
@RequestMapping("/admin/report")
@Slf4j
public class ReportController {
    @Autowired
    private ReportService reportService;

    /**
     * 统计指定时间营业额
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/turnoverStatistics")
    public Result<TurnoverReportVO> turnoverStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end){
        log.info("统计指定时间营业额{},{}",begin,end);
        TurnoverReportVO reportVO= reportService.getTurnoverStatistics(begin,end);
        return Result.success(reportVO);
    }

    /**
     * 统计指定时间用户数量
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/userStatistics")
    public Result<UserReportVO> userStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                               @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end){
        log.info("统计指定时间用户数量{},{}",begin,end);
        UserReportVO userReportVO= reportService.getUserStatistics(begin,end);
        return Result.success(userReportVO);
    }

    /**
     * 统计指定时间订单统计
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/ordersStatistics")
    public Result<OrderReportVO> ordersStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                                  @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end){
        log.info("统计指定时间订单统计{},{}",begin,end);
        OrderReportVO orderReportVO= reportService.ordersStatistics(begin,end);
        return Result.success(orderReportVO);
    }

    /**
     * 统计销量
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/top10")
    public Result<SalesTop10ReportVO> top10(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                            @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end){
        log.info("统计销量top10{},{}",begin,end);
        SalesTop10ReportVO salesTop10ReportVO= reportService.top10(begin,end);
        return Result.success(salesTop10ReportVO);
    }

    /**
     * 导出excel
     * @param response
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response){
        reportService.export(response);
    }


}
