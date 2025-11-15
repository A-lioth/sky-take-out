package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.Autofill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    /**
     * 根据id动态修改分类信息
     *
     * @param category 分类信息
     */
    @Autofill(value = OperationType.UPDATE)
    void update(Category category);

    /**
     * 分页查询
     *
     * @param categoryPageQueryDTO 分页查询参数
     * @return 分页结果
     */
    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 添加分类
     *
     * @param category 分类信息
     */
    @Autofill(value = OperationType.INSERT)
    @Insert("insert into category (type, name, sort, create_time, update_time, create_user, update_user) values (#{type}, #{name}, #{sort}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void insert(Category category);

    /**
     * 删除分类
     *
     * @param id 分类id
     */
    @Delete("delete from category where id = #{id}")
    void delete(Long id);

    /**
     * 根据类型查询分类列表
     *
     * @param type 分类类型
     * @return 分类列表
     */
    List<Category> list(Integer type);
}
