package com.sky.controller.user;

import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/user/category")
public class UserCategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 查询分类
     *
     * @param type 分类类型
     * @return 分类列表
     */
    @GetMapping("list")
    public Result<List<Category>> list(Integer type) {
        List<Category> categoryList = categoryService.list(type);
        return Result.success(categoryList);
    }
}
