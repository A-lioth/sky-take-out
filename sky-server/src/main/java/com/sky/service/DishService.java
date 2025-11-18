package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

public interface DishService {
    /**
     * 新增菜品和对应的口味
     *
     * @param dishDTO 菜品数据
     */
    void insert(DishDTO dishDTO);

    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO 分页查询参数
     * @return 分页结果
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 批量删除菜品
     *
     * @param ids 菜品id数组
     */
    void delete(Long[] ids);

    /**
     * 根据id查询菜品和对应的口味数据
     *
     * @param id 菜品id
     * @return 菜品数据
     */
    DishVO getByIdWithFlavor(Long id);

    /**
     * 修改菜品和口味数据
     *
     * @param dishDTO 菜品数据
     */
    void updateWithFlavor(DishDTO dishDTO);
}
