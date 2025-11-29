package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO 分页查询参数
     * @return 分页结果
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 新增套餐
     * @param setmealDTO 套餐数据
     */
    void insertWithDish(SetmealDTO setmealDTO);

    /**
     * 修改套餐
     * @param setmealDTO 套餐数据
     */
    void update(SetmealDTO setmealDTO);

    /**
     * 根据id查询套餐数据
     * @param id 套餐id
     * @return 套餐数据
     */
    SetmealVO getByIdWithDish(Long id);

    /**
     * 批量删除套餐
     * @param ids 套餐id数组
     */
    void deleteBatch(Long[] ids);

    /**
     * 修改套餐起售停售状态
     * @param status 状态
     * @param id 套餐id
     */
    void updateStatus(Integer status, Long id);

    /**
     * 根据分类id查询套餐
     * @param categoryId 分类id
     * @return 套餐数据
     */
    List<Setmeal> listSetmealByCategoryId(Long categoryId);

    /**
     * 根据套餐id查询包含的菜品列表
     * @param id 套餐id
     * @return 菜品列表
     */
    List<DishItemVO> getSetmealDishListById(Long id);
}
