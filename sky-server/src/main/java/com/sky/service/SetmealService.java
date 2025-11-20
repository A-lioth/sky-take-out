package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

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
}
