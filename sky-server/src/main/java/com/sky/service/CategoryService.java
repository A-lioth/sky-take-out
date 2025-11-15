package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;

public interface CategoryService {
    /**
     * 修改分类
     *
     * @param categoryDTO 修改的分类数据
     */
    void update(CategoryDTO categoryDTO);

    /**
     * 分页查询
     *
     * @param categoryPageQueryDTO 查询参数
     * @return PageResult
     */
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 启用禁用分类
     *
     * @param status 状态
     * @param id     分类id
     */
    void updateStatus(Integer status, Long id);

    /**
     * 新增分类
     *
     * @param categoryDTO 新增的分类数据
     */
    void add(CategoryDTO categoryDTO);

}
