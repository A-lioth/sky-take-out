package com.sky.mapper;

import com.sky.entity.SetmealDish;
import com.sky.vo.DishItemVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    /**
     * 根据菜品id查询套餐id
     *
     * @param dishIds 菜品id
     * @return 套餐id
     */
    List<Long> getSetmealIdsByDishIds(Long[] dishIds);

    /**
     * 批量插入套餐菜品关系数据
     *
     * @param setmealDishes 套餐菜品关系数据
     */
    void insertBatch(List<SetmealDish> setmealDishes);

    /**
     * 根据套餐id删除套餐菜品关系数据
     *
     * @param setmealId 套餐id
     */
    @Delete("delete from setmeal_dish where setmeal_id = #{setmealId}")
    void deleteBySetmealId(Long setmealId);

    /**
     * 根据套餐id查询套餐菜品关系数据
     *
     * @param id 套餐id
     * @return 套餐菜品关系数据
     */
    @Select("select * from setmeal_dish where setmeal_id = #{id}")
    List<SetmealDish> getBySetmealId(Long id);

    /**
     * 批量删除套餐菜品关系数据
     *
     * @param ids 套餐id数组
     */
    void deleteBatchBySetmealIds(Long[] ids);

    /**
     * 根据套餐id查询包含的菜品列表
     *
     * @param setmealId 套餐id
     * @return 菜品列表
     */
    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from setmeal_dish sd left join dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getSetmealDishListById(Long setmealId);
}
