package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 修改分类
     *
     * @param categoryDTO 修改的分类信息
     */
    public void update(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);

        // category.setUpdateTime(LocalDateTime.now());
        // category.setUpdateUser(BaseContext.getCurrentId());

        categoryMapper.update(category);
    }

    /**
     * 分页查询
     *
     * @param categoryPageQueryDTO 查询参数
     * @return 分页结果
     */
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
        Page<Category> page = categoryMapper.pageQuery(categoryPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 启用禁用分类
     *
     * @param status 状态
     * @param id     分类id
     */
    public void updateStatus(Integer status, Long id) {
        Category category = Category.builder()
                .id(id)
                .status(status)
                // .updateTime(LocalDateTime.now())
                // .updateUser(BaseContext.getCurrentId())
                .build();
        categoryMapper.update(category);
    }

    /**
     * 新增分类
     *
     * @param categoryDTO 新增的分类数据
     */
    public void insert(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);

        // * 设置分类状态默认为禁用
        category.setStatus(StatusConstant.DISABLE);

        // category.setCreateTime(LocalDateTime.now());
        // category.setUpdateTime(LocalDateTime.now());
        // category.setCreateUser(BaseContext.getCurrentId());
        // category.setUpdateUser(BaseContext.getCurrentId());
        categoryMapper.insert(category);
    }

    /**
     * 删除分类
     *
     * @param id 分类id
     */
    public void delete(Long id) {
        categoryMapper.delete(id);
    }

    /**
     * 根据类型查询分类列表
     *
     * @param type 分类类型
     * @return 分类列表
     */
    public List<Category> list(Integer type) {
        return categoryMapper.list(type);
    }
}
