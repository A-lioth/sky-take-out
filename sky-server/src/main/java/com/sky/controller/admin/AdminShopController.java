package com.sky.controller.admin;

import com.sky.constant.ShopConstant;
import com.sky.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/shop")
public class AdminShopController {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 设置营业状态
     *
     * @param status 状态
     * @return 结果
     */
    @PutMapping("/{status}")
    public Result<String> setStatus(@PathVariable Integer status) {
        redisTemplate.opsForValue().set(ShopConstant.SHOP_STATUS, status);
        return Result.success();
    }

    /**
     * 获取营业状态
     *
     * @return 状态
     */
    @GetMapping("/status")
    public Result<Integer> getStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(ShopConstant.SHOP_STATUS);
        return Result.success(status);
    }
}
