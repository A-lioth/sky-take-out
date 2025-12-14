package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.Autofill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     *
     * @param categoryId 分类id
     * @return 菜品数量
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 插入菜品数据
     *
     * @param dish 菜品实体
     */
    @Autofill(value = OperationType.INSERT)
    void insert(Dish dish);

    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO 分页查询参数
     * @return 分页结果
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据id查询菜品
     *
     * @param id 菜品id
     * @return 菜品数据
     */
    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    /**
     * 根据id删除菜品数据
     *
     * @param id 菜品id
     */
    @Delete("delete from dish where id = #{id}")
    void deleteById(Long id);

    /**
     * 批量删除菜品
     *
     * @param ids 菜品id数组
     */
    void deleteByIds(Long[] ids);

    /**
     * 根据菜品id查询菜品和口味数据
     *
     * @param dishId 菜品id
     * @return 菜品数据
     */
    @Select("select * from dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> getFlavorsByDishId(Long dishId);

    /**
     * 根据id动态修改菜品数据
     *
     * @param dish 菜品数据
     */
    @Autofill(value = OperationType.UPDATE)
    void update(Dish dish);

    /**
     * 根据分类id查询菜品
     *
     * @param dish 菜品
     * @return 菜品列表
     */
    List<Dish> list(Dish dish);

    /**
     * 根据套餐id查询菜品
     *
     * @param id 套餐id
     * @return 菜品列表
     */
    @Select("select a.* from dish a left join setmeal_dish b on a.id = b.dish_id where b.setmeal_id = #{setmealId}")
    List<Dish> getBySetmealId(Long id);

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId 分类id
     * @return 菜品列表
     */
    @Select("select * from dish where category_id = #{categoryId}")
    List<Dish> listByCategoryId(Long categoryId);

    /**
     * 根据条件统计菜品数量
     *
     * @param map 查询条件
     * @return 菜品数量
     */
    Integer countByMap(Map map);
}
