package com.sky.controller.user;

import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/setmeal")
public class UserSetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * 根据分类id查询套餐
     *
     * @param categoryId 分类id
     * @return 套餐列表
     */
    @RequestMapping("/list")
    public Result<List<Setmeal>> list(Long categoryId) {
        List<Setmeal> list = setmealService.listSetmealByCategoryId(categoryId);
        return Result.success(list);
    }

    /**
     * 根据套餐id查询包含的菜品列表
     *
     * @param id 套餐id
     * @return 菜品列表
     */
    @RequestMapping("/dish/{id}")
    public Result<List<DishItemVO>> getSetmealDishList(@PathVariable Long id) {
        List<DishItemVO> list = setmealService.getSetmealDishListById(id);
        return Result.success(list);
    }
}
