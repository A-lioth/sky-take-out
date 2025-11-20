package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;

    /**
     * 套餐分页查询
     *
     * @param setmealPageQueryDTO 分页查询参数
     * @return 分页结果
     */
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 新增套餐
     *
     * @param setmealDTO 套餐数据
     */
    @Transactional
    public void insertWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        // * 插入套餐数据
        setmealMapper.insert(setmeal);
        // * 获取当前新增的套餐id
        Long setmealId = setmeal.getId();
        // * 批量插入套餐和菜品的关联数据
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes != null && !setmealDishes.isEmpty()) {
            setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealId));
            setmealDishMapper.insertBatch(setmealDishes);
        }
    }

    /**
     * 修改套餐
     *
     * @param setmealDTO 套餐数据
     */
    @Transactional
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        // * 修改套餐数据
        setmealMapper.update(setmeal);
        // * 获取当前修改的套餐id
        Long setmealId = setmeal.getId();
        // * 删除套餐和菜品的关联数据
        setmealDishMapper.deleteBySetmealId(setmealId);
        // * 批量插入套餐和菜品的关联数据
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealId));
        setmealDishMapper.insertBatch(setmealDishes);
    }

    /**
     * 根据id查询套餐数据
     *
     * @param id 套餐id
     * @return 套餐数据
     */
    public SetmealVO getByIdWithDish(Long id) {
        // * 根据id查询套餐数据
        Setmeal setmeal = setmealMapper.getById(id);
        // * 创建SetmealVO对象，用于封装套餐数据
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        // * 根据套餐id查询套餐和菜品的关联数据
        setmealVO.setSetmealDishes(setmealDishMapper.getBySetmealId(id));
        return setmealVO;
    }

    /**
     * 批量删除套餐
     *
     * @param ids 套餐id数组
     */
    @Transactional
    public void deleteBatch(Long[] ids) {
        // * 当前套餐是否在售
        for (Long id : ids) {
            Setmeal setmeal = setmealMapper.getById(id);
            if (setmeal.getStatus().equals(StatusConstant.ENABLE)) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        // * 删除套餐数据
        setmealMapper.deleteBatch(ids);
        // * 删除套餐和菜品的关联数据
        setmealDishMapper.deleteBatchBySetmealIds(ids);
    }

    /**
     * 修改套餐起售停售状态
     *
     * @param status 状态
     * @param id     套餐id
     */
    public void updateStatus(Integer status, Long id) {
        // * 判断套餐内是否有在售菜品
        if (status.equals(StatusConstant.ENABLE)) {
            // * 获取套餐内所有菜品
            List<Dish> dishes = dishMapper.getBySetmealId(id);
            if (dishes != null && !dishes.isEmpty()) {
                for (Dish dish : dishes) {
                    if (dish.getStatus().equals(StatusConstant.DISABLE)) {
                        throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
                    }
                }
            }
        }
        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.update(setmeal);
    }
}
