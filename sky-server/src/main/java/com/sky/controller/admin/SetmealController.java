package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id查询套餐和关联的菜品数据
     *
     * @param id 套餐id
     * @return 套餐数据
     */
    @GetMapping("/{id}")
    public Result<SetmealVO> getById(@PathVariable Long id) {
        SetmealVO setmealVO = setmealService.getByIdWithDish(id);
        return Result.success(setmealVO);
    }

    /**
     * 套餐分页查询
     *
     * @param setmealPageQueryDTO 套餐查询参数
     * @return 套餐分页查询结果
     */
    @GetMapping("page")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 新增套餐
     *
     * @param setmealDTO 套餐数据
     * @return 添加结果
     */
    @PostMapping
    public Result<String> insert(@RequestBody SetmealDTO setmealDTO) {
        setmealService.insertWithDish(setmealDTO);
        return Result.success();
    }

    /**
     * 修改套餐
     *
     * @param setmealDTO 套餐数据
     * @return 修改结果
     */
    @PutMapping
    public Result<String> update(@RequestBody SetmealDTO setmealDTO) {
        setmealService.update(setmealDTO);
        return Result.success();
    }

    /**
     * 删除套餐
     *
     * @param ids 套餐id数组
     * @return 删除结果
     */
    @DeleteMapping
    public Result<String> delete(Long[] ids) {
        setmealService.deleteBatch(ids);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    public Result<String> updateStatus(@PathVariable Integer status, Long id) {
        setmealService.updateStatus(status, id);
        return Result.success();
    }
}
