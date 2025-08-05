package com.sky.controller.admin;


import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "店铺接口")
@Slf4j
public class ShopController {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 设置店铺状态
     * @return
     */
    @PutMapping("/{status}")
    @ApiOperation("设置店铺状态")
    public Result setStatus(@PathVariable Integer status){
        log.info("设置营业状态{}",status);
        redisTemplate.opsForValue().set("SHOP STATUS", status);
        return Result.success();
    }

    /**
     * 查询店铺状态
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("查询店铺状态")
    public Result<Integer> getStatus(){
        Integer shopStatus = (Integer) redisTemplate.opsForValue().get("SHOP STATUS");
        log.info("店铺状态{}",shopStatus);
        return Result.success(shopStatus);
    }
}
