package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;

import java.util.List;

public interface ShoppingCartService {
    /**
     * 添加购物车
     *
     * @param shoppingCartDTO 购物车数据
     */
    void insert(ShoppingCartDTO shoppingCartDTO);

    /**
     * 查看购物车
     *
     * @return 购物车数据
     */
    Result<List<ShoppingCart>> list();

    /**
     * 清空购物车
     */
    void clean();

    /**
     * 删除购物车中的单条数据
     *
     * @param shoppingCartDTO 购物车数据
     */
    void sub(ShoppingCartDTO shoppingCartDTO);
}
