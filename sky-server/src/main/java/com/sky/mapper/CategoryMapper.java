package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper {
    /**
     * 根据id动态修改分类信息
     *
     * @param category
     */
    void update(Category category);

    /**
     * 分页查询
     *
     * @param categoryPageQueryDTO
     * @return
     */
    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 添加分类
     *
     * @param category 分类信息
     */
    @Insert("insert into category (type, name, sort, create_time, update_time, create_user, update_user) values (#{type}, #{name}, #{sort}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void add(Category category);
}
