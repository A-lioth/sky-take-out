package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.Autofill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     *
     * @param id 分类id
     * @return 套餐数量
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 修改套餐信息
     *
     * @param setmeal 套餐信息
     */
    @Autofill(value = OperationType.UPDATE)
    void update(Setmeal setmeal);

    /**
     * 套餐分页查询
     *
     * @param setmealPageQueryDTO 分页查询参数
     * @return 套餐分页数据
     */
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 新增套餐
     *
     * @param setmeal 套餐信息
     */
    @Autofill(value = OperationType.INSERT)
    void insert(Setmeal setmeal);

    /**
     * 根据id查询套餐数据
     *
     * @param id 套餐id
     * @return 套餐数据
     */
    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);

    /**
     * 批量删除套餐
     *
     * @param ids 套餐id数组
     */
    void deleteBatch(Long[] ids);

    /**
     * 根据分类id查询套餐
     *
     * @param setmeal 套餐信息
     * @return 套餐数据
     */
    List<Setmeal> listSetmealByCategoryId(Setmeal setmeal);

    /**
     * 根据条件统计套餐数量
     *
     * @param map 查询条件
     * @return 套餐数量
     */
    Integer countByMap(Map map);
}
