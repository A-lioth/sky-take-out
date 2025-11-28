package com.sky.controller.user;

import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/dish")
public class UserDishController {
    @Autowired
    private DishService dishService;

    /**
     * 根据分类id查询菜品数据
     *
     * @param categoryId 分类id
     * @return 菜品数据
     */
    @GetMapping("/list")
    public Result<List<DishVO>> list(Long categoryId) {
        // * 根据分类id查询菜品数据
        List<Dish> dishList = dishService.listByCategoryId(categoryId);
        // * 根据菜品数据，查询对应的口味数据
        List<DishVO> dishVOList = dishService.listWithFlavor(dishList);
        // * 返回结果
        return Result.success(dishVOList);
    }
}
