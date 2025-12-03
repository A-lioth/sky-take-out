package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     *
     * @param shoppingCartDTO 购物车数据传输对象
     * @return 添加结果
     */
    @PostMapping("/add")
    public Result<String> insert(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        shoppingCartService.insert(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 查看购物车
     *
     * @return 购物车数据
     */
    @GetMapping("/list")
    public Result<List<ShoppingCart>> list() {
        return shoppingCartService.list();
    }

    /**
     * 清空购物车
     *
     * @return 删除结果
     */
    @DeleteMapping("/clean")
    public Result<String> clean() {
        shoppingCartService.clean();
        return Result.success();
    }

    /**
     * 删除购物车中的一个商品
     *
     * @param shoppingCartDTO 购物车数据传输对象
     * @return 删除结果
     */
    @PostMapping("/sub")
    public Result<String> sub(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        shoppingCartService.sub(shoppingCartDTO);
        return Result.success();
    }
}
