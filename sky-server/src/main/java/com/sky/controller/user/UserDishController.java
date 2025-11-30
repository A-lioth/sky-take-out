package com.sky.controller.user;

import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/dish")
public class UserDishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据分类id查询菜品数据
     *
     * @param categoryId 分类id
     * @return 菜品数据
     */
    @GetMapping("/list")
    public Result<List<DishVO>> list(Long categoryId) {
        // * 缓存优化
        List<DishVO> dishVOList = (List<DishVO>) redisTemplate.opsForValue().get("dish_" + categoryId);
        // * 缓存存在
        if (dishVOList != null && !dishVOList.isEmpty()) {
            return Result.success(dishVOList);
        }
        // * 缓存不存在
        // * 根据分类id查询菜品数据
        List<Dish> dishList = dishService.listByCategoryId(categoryId);
        // * 根据菜品数据，查询对应的口味数据
        dishVOList = dishService.listWithFlavor(dishList);
        // * 缓存数据
        redisTemplate.opsForValue().set("dish_" + categoryId, dishVOList);
        // * 返回结果
        return Result.success(dishVOList);
    }
}
