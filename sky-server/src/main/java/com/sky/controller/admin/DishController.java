package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜品管理
 */
@RestController
@RequestMapping("/admin/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishMapper dishMapper;

    /**
     * 新增菜品
     *
     * @param dishDTO 菜品数据
     * @return 结果
     */
    @PostMapping
    public Result<String> insert(@RequestBody DishDTO dishDTO) {
        dishService.insert(dishDTO);
        return Result.success();
    }

    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO 分页查询参数
     * @return 分页结果
     */
    @GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 删除菜品
     *
     * @param ids ids
     * @return 删除结果
     */
    @DeleteMapping
    public Result<String> delete(Long[] ids) {
        dishService.delete(ids);
        return Result.success();
    }

    /**
     * 根据id查询菜品及其口味信息
     *
     * @param id 菜品id
     * @return 菜品及其口味信息
     */
    @GetMapping("/{id}")
    public Result<DishVO> getById(@PathVariable Long id) {
        // * 根据id查询菜品数据
        Dish dish = dishMapper.getById(id);
        // * 根据菜品id查询口味数据
        List<DishFlavor> flavors = dishMapper.getFlavorsByDishId(id);
        // * 封装返回结果
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(flavors);

        return Result.success(dishVO);
    }

    /**
     * 修改菜品
     *
     * @param dishDTO 菜品数据
     * @return 修改结果
     */
    @PutMapping
    public Result<String> update(@RequestBody DishDTO dishDTO) {
        dishService.updateWithFlavor(dishDTO);
        return Result.success();
    }

    /**
     * 修改菜品状态
     *
     * @param status 状态
     * @param id 菜品id
     * @return 修改结果
     */
    @PostMapping("/status/{status}")
    public Result<String> updateStatus(@PathVariable Integer status, Long id) {
        dishService.updateStatus(status, id);
        return Result.success();
    }

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId 分类id
     * @return 菜品
     */
    @GetMapping("/list")
    public Result<List<Dish>> getByCategoryId(Long categoryId) {
        List<Dish> list = dishService.list(categoryId);
        return Result.success(list);
    }
}
